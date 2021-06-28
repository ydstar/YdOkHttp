package com.okhttp.download.download;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Author: 信仰年轻
 * Date: 2021-06-28 17:43
 * Email: hydznsqk@163.com
 * Des:
 */
public class DownLoadTask {

    //CPU核心数,参考来自AsyncTask源码
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //线程池中的线程总数量
    private static final int THREAD_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));

    //线程池
    private ExecutorService executorService;

    private String mUrl;
    private long mContentLength;
    private DownloadCallback mCallback;

    private volatile int mSucceedNumber;

    public DownLoadTask(String url, long contentLength, DownloadCallback callback) {
        this.mUrl = url;
        this.mContentLength = contentLength;
        this.mCallback = callback;
    }

    /**
     * 创建线程池,线程总数量为THREAD_SIZE个,然后存活时间为30s,参考自okhttp的Dispatcher类
     */
    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, THREAD_SIZE, 30, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "YD_DownLoadTask");
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return executorService;
    }

    /**
     * 初始化,并开始执行任务
     */
    public void init() {
        for (int x = 0; x < THREAD_SIZE; x++) {
            //总长度 / 线程数 = 每个线程数要下载的字节内容长度
            long threadSize = mContentLength / THREAD_SIZE;

            /**
             * 假如当前线程数为3,然后总mContentLength是300字节,那每个线程需要下载100字节
             * 那第0个区间就是[0-99],第1个区间[100-199],第2个区间[200-299]
             */
            //开始下载的字节点
            long start = x * threadSize;
            //结束下载的字节点
            long end = (threadSize + x * threadSize) - 1;

            if (x == THREAD_SIZE - 1) {
                end = mContentLength - 1;
            }

            //字节总数=22347996   threadSize = 7449332   start=0  end =7449331
            //字节总数=22347996   threadSize = 7449332   start=7449332  end =14898663
            //字节总数=22347996   threadSize = 7449332   start=14898664  end =22347995
            System.out.println("字节总数="+mContentLength+"   threadSize = " + threadSize + "   start=" + start + "  end =" + end);

            //创建runnable对象
            DownloadRunnable runnable = new DownloadRunnable(mUrl, x, start, end, new DownloadCallback() {
                @Override
                public void onFailure(IOException e) {
                    // 一个apk 下载里面有一个线程异常了，处理异常,把其他线程停止掉
                    mCallback.onFailure(e);
                }

                @Override
                public void onSucceed(File file) {
                    synchronized (DownLoadTask.class) {
                        mSucceedNumber += 1;
                        if (mSucceedNumber == THREAD_SIZE) {
                            mCallback.onSucceed(file);
                        }
                    }
                }
            });

            //开启执行这个线程
            executorService().execute(runnable);
        }
    }
}

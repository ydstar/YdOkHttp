package com.okhttp.download.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.okhttp.download.download.DownLoadTask.THREAD_SIZE;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 17:42
 * Email: hydznsqk@163.com
 * Des:
 */
public class DownLoadDispatcher {


    //下载准备队列
    private final Deque<DownLoadTask> readyTasks = new ArrayDeque<>();
    //下载正在运行队列
    private final Deque<DownLoadTask> runningTasks = new ArrayDeque<>();
    //下载停止队列
    private final Deque<DownLoadTask> stopTasks = new ArrayDeque<>();

    //专门开了个线程池来更新进度条
    private static ExecutorService sLocalProgressPool = Executors.newFixedThreadPool(THREAD_SIZE);

    private DownLoadDispatcher() {
    }

    public static DownLoadDispatcher getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final DownLoadDispatcher INSTANCE = new DownLoadDispatcher();
    }

    /**
     * 开始下载
     */
    public void startDownLoad(final String url, final DownloadCallback callback) {
        Call call = OkHttpManager.getInstance().asyncCall(url);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取文件的总大小
                final long contentLength = response.body().contentLength();
                if (contentLength <= -1) {
                    // 没有获取到文件的大小，
                    // 1. 跟后台商量
                    // 2. 只能采用单线程去下载
                    return;
                }
                DownLoadTask downLoadTask = new DownLoadTask(url, contentLength, callback);
                //开启线程池去下载
                downLoadTask.init();
                //添加到运行队列
                runningTasks.add(downLoadTask);
                //更新进度
                updateProgress(url, callback, contentLength);
            }
        });
    }

    /**
     * 进度更新
     */
    private void updateProgress(final String url, final DownloadCallback callback, final long contentLength) {
        sLocalProgressPool.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(300);
                        File file = FileManager.getInstance().getFile(url);
                        long fileSize = file.length();
                        int progress = (int) (fileSize * 100.0 / contentLength);
                        if (progress >= 100) {
                            callback.progress(progress);
                            return;
                        }
                        callback.progress(progress);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 移除正在运行的task
     */
    public void recyclerTask(DownLoadTask downloadTask) {
        runningTasks.remove(downloadTask);
        // 参考 OkHttp 的 Dispatcher 的源码,如果还有需要下载的开始下一个的下载
    }


}

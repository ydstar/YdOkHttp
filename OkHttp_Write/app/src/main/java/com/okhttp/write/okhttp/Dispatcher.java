package com.okhttp.write.okhttp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:35
 * Email: hydznsqk@163.com
 * Des: 分发器,主要是用线程池
 */
public class Dispatcher {

    private ExecutorService executorService;

    /**
     * 用线程池开启异步请求,然后就会AsyncCall中的run方法
     * @param call
     */
    public void enqueue(RealCall.AsyncCall call) {
        executorService().execute(call);
    }

    /**
     * 创建线程池,而且是单例
     */
    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "okhttp");
                    thread.setDaemon(false);
                    return thread;
                }
            });
        }
        return executorService;
    }

}

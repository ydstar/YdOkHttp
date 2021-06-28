package com.okhttp.download.download;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 17:42
 * Email: hydznsqk@163.com
 * Des:
 */
public class DownLoadDispatcher {

    private DownLoadDispatcher() {
    }

    public static DownLoadDispatcher getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final DownLoadDispatcher INSTANCE = new DownLoadDispatcher();
    }

    //下载准备队列
    private final Deque<DownLoadTask> readyTasks = new ArrayDeque<>();
    //下载正在运行队列
    private final Deque<DownLoadTask> runningTasks = new ArrayDeque<>();
    //下载停止队列
    private final Deque<DownLoadTask> stopTasks = new ArrayDeque<>();


    /**
     * 开始下载
     *
     * @param url
     * @param callback
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
                long contentLength = response.body().contentLength();
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
            }
        });
    }


}

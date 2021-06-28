package com.okhttp.download.download;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.Response;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 18:16
 * Email: hydznsqk@163.com
 * Des:
 */
public class DownloadRunnable implements Runnable {

    private static final int STATUS_DOWNLOADING = 1;
    private static final int STATUS_STOP = 2;
    private int mStatus = STATUS_DOWNLOADING;

    private long mProgress = 0;

    private String mUrl;
    private final int mThreadId;
    private final long mStart;
    private final long mEnd;
    private DownloadCallback mCallback;


    public DownloadRunnable(String url, int threadId, long start, long end, DownloadCallback callback) {
        this.mUrl = url;
        this.mThreadId = threadId;
        this.mStart = start;// 1M-2M 0.5M  1.5M - 2M
        this.mEnd = end;
        this.mCallback = callback;
    }

    @Override
    public void run() {
        RandomAccessFile accessFile = null;
        InputStream inputStream = null;
        try {
            Response response = OkHttpManager.getInstance().syncResponse(mUrl, mStart, mEnd);
            Log.e("TAG", this.toString());

            inputStream = response.body().byteStream();

            // 写数据
            File file = FileManager.manager().getFile(mUrl);
            accessFile = new RandomAccessFile(file, "rwd");
            // 从这里开始
            accessFile.seek(mStart);

            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = inputStream.read(buffer)) != -1) {
                if (mStatus == STATUS_STOP) {
                    break;
                }
                // 保存进度，做断点 , 100kb
                mProgress += len;
                accessFile.write(buffer, 0, len);
            }
            //数据写完,回调出去
            mCallback.onSucceed(file);
        } catch (IOException e) {
            mCallback.onFailure(e);
        } finally {
            Utils.close(inputStream);
            Utils.close(accessFile);
        }
    }

    public void stop() {
        mStatus = STATUS_STOP;
    }

    @Override
    public String toString() {
        return "DownloadRunnable{" +
                "mUrl='" + mUrl + '\'' +
                ", mThreadId=" + mThreadId +
                ", mStart=" + mStart +
                ", mEnd=" + mEnd +
                ", mCallback=" + mCallback +
                '}';
    }
}

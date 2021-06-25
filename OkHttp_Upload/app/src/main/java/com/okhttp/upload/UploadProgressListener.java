package com.okhttp.upload;

/**
 * Author: 信仰年轻
 * Date: 2021-06-25 18:20
 * Email: hydznsqk@163.com
 * Des: 上传进度的回调接口
 */
public interface UploadProgressListener {
    void onProgress(long total, long current);
}

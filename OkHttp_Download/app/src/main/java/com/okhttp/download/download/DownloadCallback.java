package com.okhttp.download.download;

import java.io.File;
import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 17:34
 * Email: hydznsqk@163.com
 * Des:
 */
public interface DownloadCallback {

    void onFailure(IOException e);

    void onSucceed(File file);

    void progress(int progress);
}

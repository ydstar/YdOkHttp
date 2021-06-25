package com.okhttp.write.okhttp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 14:36
 * Email: hydznsqk@163.com
 * Des:
 */
public interface Bindry {

    long fileLength();

    String mimType();

    String fileName();

    void onWrite(OutputStream outputStream) throws IOException;
}

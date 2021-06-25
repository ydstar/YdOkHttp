package com.okhttp.write.okhttp.interceptor;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:07
 * Email: hydznsqk@163.com
 * Des: 拦截器顶层接口
 */
public interface Interceptor {

    Response intercept(Chain chain) throws IOException;

    interface Chain {
        Request request();

        Response proceed(Request request) throws IOException;
    }

}

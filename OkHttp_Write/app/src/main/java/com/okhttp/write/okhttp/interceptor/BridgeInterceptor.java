package com.okhttp.write.okhttp.interceptor;

import android.util.Log;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.RequestBody;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:09
 * Email: hydznsqk@163.com
 * Des:做一个简单的处理,设置一些通用的请求头,Content-Type,Connection,Content-Length,Cookie
 * 做一些返回的处理,如果返回的数据被压缩了采用 ZipSource,保存Cookie
 */
public class BridgeInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG", "BridgeInterceptor");
        Request request = chain.request();
        //添加一些请求头
        request.header("Connection", "keep-alive");
        if (request.requestBody() != null) {
            RequestBody requestBody = request.requestBody();
            //文件的类型
            request.header("Content-Type", requestBody.getContentType());
            //要塞给对方多少东西
            request.header("Content-Length", Long.toString(requestBody.getContentLength()));
        }
        return chain.proceed(request);
    }
}

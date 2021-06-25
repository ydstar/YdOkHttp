package com.okhttp.write.okhttp.interceptor;

import android.util.Log;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:49
 * Email: hydznsqk@163.com
 * Des:findHealthyConnection()找一个连接,首先判断有没有健康的,没有就创建(建立Socket,握手连接),连接缓存
 * okHttp是基于原生的 Socket + okio (原生IO的封装)
 * 封装 HttpCodec 里面封装了okio的 Source(输入) 和 Sink(输出),我们通过 HttpCodec 就可以操作 Socket的输入输出,我们就可以向服务器写数据和读取返回数据
 */
public class ConnectInterceptor implements  Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG","ConnectInterceptor");
        Request request = chain.request();
        return chain.proceed(request);
    }
}
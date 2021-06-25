package com.okhttp.write.okhttp;

import android.util.Log;

import com.okhttp.write.okhttp.interceptor.BridgeInterceptor;
import com.okhttp.write.okhttp.interceptor.CacheInterceptor;
import com.okhttp.write.okhttp.interceptor.CallServerInterceptor;
import com.okhttp.write.okhttp.interceptor.ConnectInterceptor;
import com.okhttp.write.okhttp.interceptor.Interceptor;
import com.okhttp.write.okhttp.interceptor.RealInterceptorChain;
import com.okhttp.write.okhttp.interceptor.RetryAndFollowUpInterceptor;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:31
 * Email: hydznsqk@163.com
 * Des: 真正发起请求的Call对象
 */
public class RealCall implements Call {

    private OkHttpClient client;
    private Request originalRequest;

    public RealCall(Request originalRequest,OkHttpClient client) {
        this.client = client;
        this.originalRequest = originalRequest;
    }

    public static Call newCall(Request request, OkHttpClient okHttpClient) {
        return new RealCall(request,okHttpClient);
    }

    //异步请求
    @Override
    public void enqueue(Callback callback) {
        //异步交给线程池
        AsyncCall asyncCall = new AsyncCall(callback);
        client.dispatcher.enqueue(asyncCall);
    }

    //同步请求
    @Override
    public Response execute() {
        return null;
    }

    final class AsyncCall extends NamedRunnable{

        Callback callback;

        public AsyncCall(Callback callback){
            this.callback=callback;
        }

        @Override
        protected void execute() {
            // 来这里，开始访问网络 Request -> Response
            Log.e("TAG","execute");
            // 基于 HttpUrlConnection , OkHttp = Socket + okio(IO)
            final Request request = originalRequest;
            try {
                List<Interceptor> interceptors = new ArrayList<>();
                interceptors.add(new RetryAndFollowUpInterceptor());// 重试
                interceptors.add(new BridgeInterceptor());// 基础
                interceptors.add(new CacheInterceptor());// 缓存
                interceptors.add(new ConnectInterceptor());// 建立连接
                interceptors.add(new CallServerInterceptor());// 写数据

                Interceptor.Chain chain = new RealInterceptorChain(interceptors,0,originalRequest);
                Response response = chain.proceed(request);
                callback.onResponse(RealCall.this,response);
            } catch (IOException e) {
                callback.onFailure(RealCall.this,e);
            }
        }
    }
}


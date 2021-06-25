package com.okhttp.write.okhttp.interceptor;

import android.util.Log;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:47
 * Email: hydznsqk@163.com
 * Des:处理重试的一个拦截器,会去处理一些异常,只要不是致命的异常就会重新发起一次请求(把Request给下级),如果是致命的异常就会抛给上一级;
 * 会处理一些重定向等等,比如3XX 307,407就会从头部中获取新的路径,生成一个新的请求交给下一级(重新发送一次请求)
 */
public class RetryAndFollowUpInterceptor implements  Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG","RetryAndFollowUpInterceptor");
        Request request = chain.request();
        return chain.proceed(request);
    }
}

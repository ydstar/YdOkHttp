package com.okhttp.write.okhttp.interceptor;

import android.util.Log;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:09
 * Email: hydznsqk@163.com
 * Des:在缓存可用的情况下,读取本地的缓存的数据,如果没有直接去服务器,如果有首先判断有没有缓存策略,然后判断有没有过期,
 * 如果没有过期直接拿缓存,如果过期了需要添加一些之前头部信息如:If-Modified-Since,
 * 这个时候后台有可能会给你返回 304 代表你是可以拿本地缓存,每次读取到新的响应后做一次缓存
 */
public class CacheInterceptor implements  Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG","CacheInterceptor");
        Request request = chain.request();
        // 本地有没有缓存，如果有没过期
        /*if(true){
            return new Response(new );
        }*/

        return chain.proceed(request);
    }
}

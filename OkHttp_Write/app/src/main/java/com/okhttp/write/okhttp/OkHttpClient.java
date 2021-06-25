package com.okhttp.write.okhttp;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:30
 * Email: hydznsqk@163.com
 * Des: OkHttp客户端对象
 */
public class OkHttpClient {

    Dispatcher dispatcher;

    public OkHttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
    }

    public OkHttpClient() {
        this(new Builder());
    }

    public Call newCall(Request request) {
        return RealCall.newCall(request, this);
    }

    public static class Builder {
        Dispatcher dispatcher;

        public Builder() {
            dispatcher = new Dispatcher();
        }

        public OkHttpClient builder() {
            return new OkHttpClient(this);
        }
    }
}

package com.okhttp.write.okhttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:31
 * Email: hydznsqk@163.com
 * Des:把配置的请求信息封装成Request对象,包含url,method请求方式,headers头信息,RequestBody请求体
 */
public class Request {

    final String url;//url
    final Method method;//请求方式



    final Map<String, String> headers;//头信息
    final RequestBody requestBody;//请求体,用于post请求

    private Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBody = builder.requestBody;
    }


    public String url() {
        return url;
    }

    public Method method() {
        return method;
    }

    public void header(String key, String value) {
        headers.put(key,value);
    }

    public RequestBody requestBody() {
        return requestBody;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }


    public static class Builder {

        String url;//url
        Method method;//请求方式
        Map<String, String> headers;//头信息
        RequestBody requestBody;//请求体,用于post请求

        public Builder() {
            method = Method.GET;
            headers = new HashMap<>();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            method = Method.GET;
            return this;
        }

        public Builder post(RequestBody body) {
            method = Method.POST;
            this.requestBody = body;
            return this;
        }

        public Builder headers(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public Builder headers( Map<String, String> map) {
            headers.putAll(map);
            return this;
        }
        public Request builder() {
            return new Request(this);
        }
    }
}

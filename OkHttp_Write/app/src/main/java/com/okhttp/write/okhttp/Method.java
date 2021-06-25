package com.okhttp.write.okhttp;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:29
 * Email: hydznsqk@163.com
 * Des: 请求的枚举类
 */
public enum Method {
    POST("POST"),GET("GET"),HEAD("HEAD"),DELETE("DELETE"),PUT("PUT"),PATCH("PATCH");
    public String name;

    Method(String name) {
        this.name = name;
    }

    public boolean doOutput() {
        switch (this){
            case POST:
            case PUT:
                return true;
        }
        return false;
    }
}

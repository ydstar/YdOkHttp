package com.okhttp.write.okhttp;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:28
 * Email: hydznsqk@163.com
 * Des: 请求的Call顶层接口
 */
public interface Call {
    /**
     * 发起异步请求
     * @param callback
     */
    void  enqueue(Callback callback);

    /**
     * 发起同步请求
     * @return
     */
    Response execute();
}

package com.okhttp.write.okhttp;

import java.io.IOException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:29
 * Email: hydznsqk@163.com
 * Des: 网络请求后的回调接口
 */
public interface Callback {


    /**
     * 失败
     */
    void onFailure(Call call, IOException e);


    /**
     * 正常返回
     */
    void onResponse(Call call, Response response) throws IOException;
}

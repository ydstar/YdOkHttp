package com.okhttp.download.download;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 16:14
 * Email: hydznsqk@163.com
 * Des:
 */
public class OkHttpManager {

    private OkHttpClient okHttpClient;

    private OkHttpManager() {
        okHttpClient = new OkHttpClient();
    }

    public static OkHttpManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final OkHttpManager INSTANCE = new OkHttpManager();
    }


    /**
     * 发起异步请求
     * @param url
     * @return
     */
    public Call asyncCall(String url) {
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request);
    }

    /**
     * 发起同步请求,携带着range
     * @param url
     * @param start
     * @param end
     * @return
     * @throws IOException
     */
    public Response syncResponse(String url, long start, long end) throws IOException {
        Request request = new Request
                .Builder()
                .url(url)
                .addHeader("Range", "bytes=" + start + "-" + end)
                .build();
        return okHttpClient.newCall(request).execute();
    }
}

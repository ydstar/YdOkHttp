package com.okhttp.write.okhttp.interceptor;

import android.util.Log;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.RequestBody;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:09
 * Email: hydznsqk@163.com
 * Des:写数据和读取数据
 * 写头部信息,写body表达信息等等
 */
public class CallServerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.e("TAG","CallServerInterceptor");
        // 来这里，开始访问网络 Request -> Response
        Log.e("TAG", "execute");
        // 基于 HttpUrlConnection , OkHttp = Socket + okio(IO)
        final Request request = chain.request();
        URL url = new URL(request.url());

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        if (urlConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
            // https 的一些操作
            // httpsURLConnection.setHostnameVerifier();
            // httpsURLConnection.setSSLSocketFactory();
        }
        // urlConnection.setReadTimeout();

        // 写东西
        urlConnection.setRequestMethod(request.method().name);
        urlConnection.setDoOutput(request.method().doOutput());
        //Post方式不能缓存,需手动设置为false
        urlConnection.setUseCaches(false);
        RequestBody requestBody = request.requestBody();

        //自己定义的头信息 header,里面有token和boarding-pass
        Map<String, String> headers = request.getHeaders();
        System.out.println(headers.toString());
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                urlConnection.setRequestProperty(entry.getKey(), entry.getValue());//设置请求头
            }
        }
        urlConnection.connect();

        // 写内容
        if (requestBody != null) {
            requestBody.onWriteBody(urlConnection.getOutputStream());
        }

        int statusCode = urlConnection.getResponseCode();
        System.out.println("statusCode = "+statusCode);
        if (statusCode == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            Response response = new Response(inputStream);
            return response;
        } else {
            InputStream inputStream = urlConnection.getInputStream();
            Response response = new Response(inputStream);
            return response;
        }
    }
}

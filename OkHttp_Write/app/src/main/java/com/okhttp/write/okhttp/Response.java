package com.okhttp.write.okhttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:31
 * Email: hydznsqk@163.com
 * Des: 响应,通过inputStream解析服务器返回来的数据为String
 */
public class Response {
    private final InputStream inputStream;// Skin

    public Response(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String string() {
        return convertStreamToString(inputStream);
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}

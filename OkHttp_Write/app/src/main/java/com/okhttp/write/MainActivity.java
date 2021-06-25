package com.okhttp.write;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.okhttp.write.okhttp.Call;
import com.okhttp.write.okhttp.Callback;
import com.okhttp.write.okhttp.OkHttpClient;
import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.RequestBody;
import com.okhttp.write.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:31
 * Email: hydznsqk@163.com
 * Des:
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 普通的POST请求
     */
    public void click(View view) {
        //1.创建okHttpClient和创建Request对象
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new RequestBody()
                .type(RequestBody.FORM)
                .addParam("userName", "beijing")
                .addParam("password", "123456");

        Request request = new Request
                .Builder()
                .post(requestBody)
                .headers(getHeaderParams())
                .url("https://api.devio.org/as/user/login")
                .builder();

        //2.把Request对象封装成call对象
        Call call = okHttpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", e.toString());
                show(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.string();
                Log.e("TAG", string);
                show(string);
            }
        });
    }

    private void show(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private HashMap<String, String> getHeaderParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put("auth-token", "MTU5Mjg1MDg3NDcwNw11.26==");
        map.put("boarding-pass", "");
//        map.put("boarding-pass", "8F66ADEAF01C7BAE92685449D54EF9DF");
        return map;
    }


    /**
     * 文件的上传代码示例(运行不了,只是示例)
     * @param view
     */
    public void click2(View view) {
        File file = new File("");

        //1.创建okHttpClient和创建Request对象
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new RequestBody()
                .type(RequestBody.FORM)
                .addParam("file1", RequestBody.create(file))
                .addParam("file2", RequestBody.create(file));

        Request request = new Request
                .Builder()
                .post(requestBody)
                .headers(getHeaderParams())
                .url("https://api.devio.org/as/upload/image")
                .builder();

        //2.把Request对象封装成call对象
        Call call = okHttpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TAG", e.toString());
                show(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.string();
                Log.e("TAG", string);
                show(string);
            }
        });
    }


}
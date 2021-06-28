package com.okhttp.download.single_thread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.okhttp.download.BuildConfig;
import com.okhttp.download.R;
import com.okhttp.download.download.AppInfoUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 单线程下载
 */
public class SingleThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_thread);
    }

    /**
     * 单线程下载
     */
    public void method1(View view) {
        String url = "https://oss.pgyer.com/b9189aa3d367934f317ac2b7dee990f7.apk?auth_key=1624879105-613fd7ceee04a7852be784c2b5c1d6f3-0-6738efb1c0a13b2ef1f851e599837156&response-content-disposition=attachment%3B+filename%3D%25E6%258B%259B%25E8%25B4%25A2%25E9%25B9%2585_1.1.6.apk";

        //1.1创建okHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();

        //1.2创建Request对象
        Request request = new Request.Builder().url(url).build();

        //2.把Request对象封装成call对象
        Call call = okHttpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("成功" + response.body().string());
                InputStream inputStream = response.body().byteStream();
                final File file = new File(getCacheDir(), "pipixia.apk");
                FileOutputStream outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] bytes = new byte[1024 * 10];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
                inputStream.close();
                outputStream.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppInfoUtils.install(file.getAbsolutePath());
                    }
                });
            }
        });
    }

}
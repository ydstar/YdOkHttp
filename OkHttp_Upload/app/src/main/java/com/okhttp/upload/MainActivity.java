package com.okhttp.upload;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * Author: 信仰年轻
 * Date: 2021-06-25 18:00
 * Email: hydznsqk@163.com
 * Des: 代码只是示例,url并不是真正可用的url(仅供学习)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View view) {
        //先申请权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            // 权限申请，并且用户给了权限
                            uploadFile();
                        }
                    }
                });
    }

    /**
     * 加了上传进度监听的上传文件okhttp的写法
     */
    private void uploadFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "test.png");
        //1.1创建okHttpClient
        OkHttpClient httpClient = new OkHttpClient();

        //1.2创建RequestBody对象(MultipartBody是继承RequestBody)
        MultipartBody.Builder builder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);

        builder.addFormDataPart("platform", "android");
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));

        //1.3包装MultipartBody成MyMultipartBody
        MyMultipartBody myMultipartBody = new MyMultipartBody(builder.build(), new UploadProgressListener() {
            @Override
            public void onProgress(long total, long current) {
                //回调接口打印总进度和当前进度
                Log.e("TAG", total + " : " + current);
            }
        });

        //1.4创建Request对象
        Request request = new Request.Builder()
                .url("https://www.hyd.com/xxxxx")
                .post(myMultipartBody)
                .build();

        //2.把Request对象封装成call对象
        Call call = httpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }
        });
    }

    /**
     * 原始的上传文件okhttp的写法(没有上传的监听)
     */
    private void oldUploadFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "test.png");

        //1.1创建okHttpClient
        OkHttpClient httpClient = new OkHttpClient();

        //1.2创建RequestBody对象
        MultipartBody.Builder builder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("platform", "android");
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));

        //1.3创建Request对象
        Request request = new Request.Builder()
                .url("https://www.hyd.com/xxxxx")
                .post(builder.build()).build();

        //2.把Request对象封装成call对象
        Call call = httpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", response.body().string());
            }
        });
    }

    private String guessMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimType = fileNameMap.getContentTypeFor(filePath);
        if (TextUtils.isEmpty(mimType)) {
            return "application/octet-stream";
        }
        return mimType;
    }
}
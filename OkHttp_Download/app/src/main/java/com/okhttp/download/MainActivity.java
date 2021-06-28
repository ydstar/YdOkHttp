package com.okhttp.download;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.okhttp.download.more_thread.MoreThreadActivity;
import com.okhttp.download.single_thread.SingleThreadActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 单线程下载
     */
    public void method1(View view) {
       startActivity(new Intent(this, SingleThreadActivity.class));
    }

    /**
     * 多线程下载
     */
    public void method2(View view) {
        startActivity(new Intent(this, MoreThreadActivity.class));
    }


}
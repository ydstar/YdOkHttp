package com.okhttp.download;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.okhttp.download.more_thread.MoreThreadActivity;
import com.okhttp.download.single_thread.SingleThreadActivity;

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
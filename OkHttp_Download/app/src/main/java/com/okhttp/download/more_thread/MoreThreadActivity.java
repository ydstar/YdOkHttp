package com.okhttp.download.more_thread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.okhttp.download.BuildConfig;
import com.okhttp.download.MainActivity;
import com.okhttp.download.R;
import com.okhttp.download.download.AppInfoUtils;
import com.okhttp.download.download.DownLoadFacade;
import com.okhttp.download.download.DownloadCallback;
import com.okhttp.download.download.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 多线程下载
 */
public class MoreThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_thread);
    }

    /**
     * 多线程下载
     */
    public void method1(View view) {
        String url = "https://oss.pgyer.com/b9189aa3d367934f317ac2b7dee990f7.apk?auth_key=1624879105-613fd7ceee04a7852be784c2b5c1d6f3-0-6738efb1c0a13b2ef1f851e599837156&response-content-disposition=attachment%3B+filename%3D%25E6%258B%259B%25E8%25B4%25A2%25E9%25B9%2585_1.1.6.apk";

        DownLoadFacade.getInstance().startDownLoad(url, new DownloadCallback() {
            @Override
            public void onFailure(IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onSucceed(final File file) {
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
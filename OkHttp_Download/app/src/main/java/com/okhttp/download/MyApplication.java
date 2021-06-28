package com.okhttp.download;

import android.app.Application;

import com.okhttp.download.download.DownLoadFacade;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 18:28
 * Email: houyadong@zhufaner.com
 * Des:
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DownLoadFacade.getInstance().init(this);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}

package com.okhttp.download.download;

import android.content.Context;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 17:31
 * Email: hydznsqk@163.com
 * Des:
 */
public class DownLoadFacade {

    private DownLoadFacade() {
    }

    public static DownLoadFacade getInstance() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        private static final DownLoadFacade INSTANCE = new DownLoadFacade();
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        FileManager.manager().init(context.getApplicationContext());
    }

    /**
     * 开始下载
     * @param url
     * @param callback
     */
    public void startDownLoad(String url,DownloadCallback callback){
        DownLoadDispatcher.getInstance().startDownLoad(url,callback);
    }
}

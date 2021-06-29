package com.okhttp.download.download;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.okhttp.download.MyApplication;

import java.io.Closeable;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author: 信仰年轻
 * Date: 2021-06-28 18:23
 * Email: hydznsqk@163.com
 * Des:
 */
public class Utils {

    public static String md5Url(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            messageDigest.update(url.getBytes());
            byte[] cipher = messageDigest.digest();
            for (byte b : cipher) {
                // 转成了 16 机制
                String hexStr = Integer.toHexString(b & 0xff);
                // 不足还补 0
                sb.append(hexStr.length() == 1 ? "0" + hexStr : hexStr);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void install(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            AppUtils.installApp(path,"com.yadong.install");
        }else{
            intent.setDataAndType(Uri.parse("file://" + path),
                    "application/vnd.android.package-archive");
            MyApplication.getInstance().startActivity(intent);
        }
    }
}

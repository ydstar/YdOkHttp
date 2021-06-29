package com.okhttp.download.single_thread;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.okhttp.download.R;
import com.okhttp.download.download.FileManager;
import com.okhttp.download.download.Utils;

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
        String url = "https://dl008.liqucn.com/upload/2021/286/e/com.ss.android.ugc.aweme_16.5.0_liqucn.com.apk";
//        String url = "https://ip1982921079.mobgslb.tbcache.com/fs08/2021/06/01/0/122_ece8104c9d896b0475415d925a3f2422.apk?yingid=wdj_web&fname=%E8%85%BE%E8%AE%AFWiFi%E7%AE%A1%E5%AE%B6&productid=2011&pos=wdj_web%2Fdetail_normal_dl%2F0&appid=6761165&packageid=601047683&apprd=6761165&iconUrl=http%3A%2F%2Fandroid-artworks.25pp.com%2Ffs08%2F2021%2F06%2F04%2F8%2F110_4862b243ebbc1d7e9c129ea14bb68d68_con.png&pkg=com.tencent.wifimanager&did=47b7ed4ea6f1616e87f94bdc81e5a839&vcode=177&md5=88ce8d3941372e85f18b897f168a4d1b&ali_redirect_domain=alissl.ucdl.pp.uc.cn&ali_redirect_ex_ftag=962f5defa20bfb238b8ae96dcc14fe631e588bfb9e313d24&ali_redirect_ex_tmining_ts=1624889190&ali_redirect_ex_tmining_expire=3600&ali_redirect_ex_hot=100";

        //方便测试，每次都先删除一下文件
        FileManager.getInstance().deleteFile(url);

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
//                System.out.println("成功" + response.body().string());
                InputStream inputStream = response.body().byteStream();
                final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "pipixia.apk");
                FileOutputStream outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] bytes = new byte[1024 * 10];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }
                inputStream.close();
                outputStream.close();
                Utils.install(file.getAbsolutePath());
            }
        });
    }

}
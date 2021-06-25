package com.okhttp.write.okhttp;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:31
 * Email: hydznsqk@163.com
 * Des:请求体
 */
public class RequestBody {

    // 表单格式
    public static final String FORM = "multipart/form-data";

    // 参数，文件
    private final HashMap<String, Object> params;
    private String boundary = createBoundary();//边界
    private String type;//类型
    private String startBoundary = "--" + boundary;//开始的边界
    private String endBoundary = startBoundary + "--";//结束的边界

    public RequestBody() {
        params = new HashMap<>();
    }

    private String createBoundary() {
        return "OkHttp"+ UUID.randomUUID().toString();
    }

    /**
     * 内容的格式类型
     */
    public String getContentType() {
        //multipart/form-data;boundary = OkHttp6da96684-328e-4f52-aead-4ec80201231c
        return type + ";boundary = " + boundary;
    }

    /**
     * 要塞给对方多少东西
     * 多少字节
     */
    public long getContentLength() {
        long length=0;
        Set<Map.Entry<String, Object>> entries = params.entrySet();

        for(Map.Entry<String, Object> entry:entries){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String text = getText(key, (String) value);
                Log.e("TAG",text);
                length+=text.getBytes().length;
            }
            if(value instanceof  Bindry){
                Bindry bindry =(Bindry)value;
                String text = getText(key, bindry);
                Log.e("TAG",text);
                length += text.getBytes().length;
                length += bindry.fileLength()+"\r\n".getBytes().length;
            }
        }

        if(params.size()!=0){
            length+=endBoundary.getBytes().length;
        }
        return length;
    }

    //写内容
    public void onWriteBody(OutputStream outputStream) throws IOException {
        Set<Map.Entry<String, Object>> entries = params.entrySet();

        for(Map.Entry<String, Object> entry:entries){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                String text = getText(key, (String) value);
                outputStream.write(text.getBytes());
            }
            if(value instanceof  Bindry){
                Bindry bindry =(Bindry)value;
                String text = getText(key, bindry);
                outputStream.write(text.getBytes());
                bindry.onWrite(outputStream);
                outputStream.write("\r\n".getBytes());
            }
        }
        //结尾要记得加上 endBoundary
        if(params.size()!=0){
            outputStream.write(endBoundary.getBytes());
        }
    }

    /**
     * 文本的格式如下,有点蛋疼,但是都是一些规范
     * startBoundary + "\r\n"
     * Content-Disposition; form-data; name = "userName"
     * Context-Type: text/plain
     *
     *
     * beijing
     */
    private String getText(String key, String value) {
        return startBoundary+"\r\n"+
                "Content-Disposition: form-data; name = \""+key+"\"\r\n"+
                "Context-Type: text/plain\r\n"+
                "\r\n"+
                value+
                "\r\n";
    }

    public RequestBody addParam(String key, String value) {
        params.put(key, value);
        return this;
    }
    public RequestBody type(String type) {
        this.type = type;
        return this;
    }

////以下为上传文件的方法/////////////////////////////////////////////////////

    private String getText(String key, Bindry value) {
        return startBoundary+"\r\n"+
                "Content-Disposition: form-data; name = \""+key+"\" filename = \""+value.fileName()+"\""+
                "Context-Type: "+value.mimType()+"\r\n"+
                "\r\n";
    }

    public RequestBody addParam(String key, Bindry value) {
        params.put(key,value);
        return this;
    }

    public static Bindry create(final File file) {

        return new Bindry() {
            @Override
            public long fileLength() {
                return file.length();
            }

            @Override
            public String mimType() {
                FileNameMap fileNameMap = URLConnection.getFileNameMap();
                String mimType = fileNameMap.getContentTypeFor(file.getAbsolutePath());
                if(TextUtils.isEmpty(mimType)){
                    return "application/octet-stream";
                }
                return mimType;
            }

            @Override
            public String fileName() {
                return file.getName();
            }

            @Override
            public void onWrite(OutputStream outputStream) throws IOException {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[2048];
                int len=0;
                while ((len=inputStream.read(buffer))!=-1){
                    outputStream.write(buffer,0,len);
                }
                inputStream.close();
            }
        };
    }
}

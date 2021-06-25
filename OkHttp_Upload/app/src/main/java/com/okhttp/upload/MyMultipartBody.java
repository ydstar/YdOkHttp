package com.okhttp.upload;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * Author: 信仰年轻
 * Date: 2021-06-25 18:00
 * Email: hydznsqk@163.com
 * Des:
 * 之所以让MyMultipartBody继承RequestBody,而不是直接继承MultipartBody,是因为MultipartBody是final修饰的,所以只能用这种静态代理的方式,
 * 不仅继承RequestBody,而且还要把外界的MultipartBody传进来,让MultipartBody这个类重新调用一下方法,
 * 等于说MyMultipartBody就是个中间件,真正起作用的类是传进来的MultipartBody,但是改MyMultipartBody的writeTo()等方法也会被调用的
 */
public class MyMultipartBody extends RequestBody {

    // 被代理的对象
    private RequestBody mRequestBody;

    //回调接口
    private UploadProgressListener mListener;

    //当前的长度
    private long mCurrentLength;

    //总长度
    private long mTotalLength;

    public MyMultipartBody(RequestBody requestBody, UploadProgressListener listener) {
        this.mRequestBody = requestBody;
        this.mListener = listener;
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        //总长度
        mTotalLength = contentLength();

        //这里也是静态代理
        //把sink传进去,也是跟MyMultipartBody这个静态代理如出一辙的手法,这样可以弄个中间件过一层
        ForwardingSink forwardingSink = new ForwardingSink(sink) {
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                //写内容就会来这里,其实也是sink在写
                mCurrentLength += byteCount;
                if (mListener != null) {
                    mListener.onProgress(mTotalLength, mCurrentLength);
                }
                super.write(source, byteCount);
            }
        };

        //包装成BufferedSink
        BufferedSink buffer = Okio.buffer(forwardingSink);
        // 最终调用者还是被代理对象的方法
        mRequestBody.writeTo(buffer);
        // 刷新，RealConnection 连接池
        buffer.flush();
    }
}

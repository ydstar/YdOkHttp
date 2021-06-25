package com.okhttp.write.okhttp.interceptor;

import com.okhttp.write.okhttp.Request;
import com.okhttp.write.okhttp.Response;

import java.io.IOException;
import java.util.List;

/**
 * Author: 信仰年轻
 * Date: 2021-06-24 19:09
 * Email: hydznsqk@163.com
 * Des:
 */
public class RealInterceptorChain implements Interceptor.Chain {
    final List<Interceptor> interceptorList;
    final int index;
    final Request request;

    public RealInterceptorChain(List<Interceptor> interceptorList, int index, Request request) {
        this.interceptorList = interceptorList;
        this.index = index;
        this.request = request;
    }

    @Override
    public Request request() {
        return request;
    }

    @Override
    public Response proceed(Request request) throws IOException {
        RealInterceptorChain chain = new RealInterceptorChain(interceptorList, index + 1, request);
        Interceptor interceptor = interceptorList.get(index);
        Response response = interceptor.intercept(chain);
        return response;
    }
}

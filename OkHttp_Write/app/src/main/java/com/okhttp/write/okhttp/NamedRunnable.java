package com.okhttp.write.okhttp;

/**
 * Author: 信仰年轻
 * Date: 2021-06-23 17:29
 * Email: hydznsqk@163.com
 * Des:
 */
public abstract class NamedRunnable implements Runnable {
    @Override
    public void run() {
        execute();
    }

    protected abstract void  execute();
}

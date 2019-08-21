package com.websocket.demo.Thread;

/**
 * @ClassName ThreadDemo
 * @Description TODO
 * @Author G-B-X
 * @Date 2019/7/16 11:41
 * @Version 1.0
 **/
public class ThreadDemo implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.wwyl.study.netty_study.io.aio;

/**
 * @Auther: lvla
 * @Date: 2018/11/21 16:38
 * @Description:
 */
public class AioTimeServer {

    public static void main(String[] args) {
        int port = 8080;
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServerHandler,"AIO-asyncTimeServerHandler-001").start();
    }
}

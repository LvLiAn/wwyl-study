package com.wwyl.study.netty_study.nio;

/**
 * @Auther: lvla
 * @Date: 2018/11/19 15:56
 * @Description:
 */
public class NioTimeServer {

    public static void main(String[] args) {

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(8080);
        new Thread(timeServer,"NIO-TIMESVR_1.0").start();
    }
}

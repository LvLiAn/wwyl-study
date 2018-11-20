package com.wwyl.study.netty_study.nio;

/**
 * @Auther: lvla
 * @Date: 2018/11/20 09:48
 * @Description:
 */
public class NioTimeClient {

    public static void main(String[] args) {
         new Thread(new NioTimeClientHandler("127.0.0.1",8080),"nio time client start!").start();

    }
}

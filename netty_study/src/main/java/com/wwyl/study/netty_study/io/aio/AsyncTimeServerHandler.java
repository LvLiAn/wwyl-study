package com.wwyl.study.netty_study.io.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther: lvla
 * @Date: 2018/11/21 16:40
 * @Description: 异步的非阻塞模式 NIO2.0
 */
public class AsyncTimeServerHandler implements Runnable{

    private int port;


    public CountDownLatch latch;

    public AsynchronousServerSocketChannel asyncronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try{
            asyncronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asyncronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("the aio time server is start:" + port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        // latch 作用，让线程在此阻塞，仅做示例用
        latch = new CountDownLatch(1);
        doAccept();
        try{
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void doAccept() {
//        asyncronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }
}

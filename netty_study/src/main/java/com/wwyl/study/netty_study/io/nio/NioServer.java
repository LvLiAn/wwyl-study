package com.wwyl.study.netty_study.io.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Auther: lvla
 * @Date: 2018/11/19 15:20
 * @Description:
 */
public class NioServer {

    public void start(){
        try {
            // 打开ServerSocketChannel，监听所有客户端连接，，为所有客户端父管道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 绑定监听端口，设置非阻塞模式
            serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getByName("IP"),8080));
            serverSocketChannel.configureBlocking(false);
            // 创建Reactor线程，并创建Selector多路复用器，并启动线程
            Selector selector = Selector.open();
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
            // 将ServerSocketChannel注册到 selector多路复用器上，监听accpet事情
            SelectionKey selectionKey = serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT,"");
            //

            // 监听客户端
            SocketChannel socketChannel = serverSocketChannel.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

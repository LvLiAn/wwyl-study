package com.wwyl.study.netty_study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: lvla
 * @Date: 2018/11/20 09:49
 * @Description:
 */
public class NioTimeClientHandler implements Runnable{

    private SocketChannel socketChannel;

    private Selector selector;

    private volatile boolean stop;

    private String host;
    private int port;

    public NioTimeClientHandler(String host,int port) {
        try{
            this.host = host;
            this.port = port;
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    public void run() {
        try{
            doConnect();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        while(!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (it.hasNext()){
                    selectionKey = it.next();
                    it.remove();
                    try{
                        handlerInputKey(selectionKey);
                    }catch (Exception e){
                        e.printStackTrace();
                        selectionKey.cancel();
                        if(selectionKey.channel()!=null)
                                selectionKey.channel().close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if(selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlerInputKey(SelectionKey selectionKey) throws IOException {
        if(selectionKey.isValid()){
            // 判断是否连接成功
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            if(selectionKey.isConnectable()){
                if(socketChannel.finishConnect()){
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    doWrite(socketChannel);
                }else {
                    // 连接失败 进程退出
                    System.exit(1);
                }
            }
            if(selectionKey.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int reads = socketChannel.read(byteBuffer);
                if(reads > 0){
                    // 处理服务端返回信息
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);

                    String body = new String(bytes,"utf-8");
                    System.out.println("nio client read body:"+ body);
                    this.stop = true;
                }else if(reads < 0){
                    selectionKey.cancel();
                    socketChannel.close();
                }
            }

        }
    }

    private void doConnect() throws IOException {
        // 如果直接连接成功，则socketChannel注册进多路复用器selector,
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    /**
     *  客户端发送信息到服务端
     * @param socketChannel
     * @throws IOException
     */
    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "QUERY TIME ORDER".getBytes("utf-8");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("send order 2 time server success!");
        }
    }
}

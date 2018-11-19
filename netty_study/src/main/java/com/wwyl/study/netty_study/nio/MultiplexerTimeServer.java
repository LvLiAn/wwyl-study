package com.wwyl.study.netty_study.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @Auther: lvla
 * @Date: 2018/11/19 15:59
 * @Description:
 */
public class MultiplexerTimeServer implements Runnable{

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private boolean isStop;

    public MultiplexerTimeServer(int port) {
        try{
            // 创建多路复用器
            selector = Selector.open();
            // 创建serverSocketChannel，监听客户端连接，并绑定端口，设置为非阻塞模式
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            serverSocketChannel.configureBlocking(false);
            // 将serverSocketChannel注册到多路复用器上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The nio time server is start,port is :" + port);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.isStop = true;
    }

    @Override
    public void run() {
        while (!isStop){
            try{
                selector.select(1000);
                Set<SelectionKey> selectionKeySets = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeySets.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handlerSelectorKey(key);
                    }catch (Exception e){
                        e.printStackTrace();
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        // 多路复用器关闭后，所有注册在上面的channel和pipe等资源都会自动去注册并关闭，所以不用重复释放关闭资源
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理新接入请求信息
     * @param key
     */
    public void handlerSelectorKey(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                // 接收新连接
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                // 新增客户端连接进多路复用器
                socketChannel.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(buffer);
                if(readBytes > 0){
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("the nio time server recieve order is " + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    System.out.println("The time server send currentTime:" + currentTime);
                    doWrite(socketChannel,currentTime);
                }else if(readBytes <0){
                    // 对端链路关闭
                    key.cancel();
                    socketChannel.close();
                }else{
                    // 读取到0字节
                }
            }
        }
    }

    public void doWrite(SocketChannel socketChannel,String msg) {
        if(msg != null && !"".equals(msg.trim())){
            try {
                byte[] bytes = msg.getBytes("UTF-8");
                ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                socketChannel.write(byteBuffer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

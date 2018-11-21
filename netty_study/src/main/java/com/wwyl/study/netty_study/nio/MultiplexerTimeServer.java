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
 *      准备nio服务端步骤，
 *      1、创建多路复用器
 *      2、创建serverSocketchannnel,并将ip:port 绑定到改channel上
 *      3、设置channel为非阻塞，即nio模式
 *      4、将channel注册到多路复用器selector上，并保持accept()状态
 *      线程run()方法实现对接收到客户端连接，发送过来的消息进行处理操作
 *      1、while循环，遍历selector
 *      2、间隔时间1s  selector.select(1000)
 *      3、selector.selectedKeys()  当有就绪的channel时，返回该channel的selectedKeys集合
 *      4、遍历返回的selectedKeys集合，
 *      5、处理每个就绪的连接channel，
 *
 *          判断是否是新连接，
 *                 是：通过serverSocketChannel的accept事件，请求并创建socketChannel,该操作相当于完成tcp三次握手
 *                    对新建socketChannel设置为异步阻塞、缓冲区大小等参数，并将该socketChannel注册进多路复用器
 *                    设置read状态
 *      6、读取channel信息：（注：以下操作只是简单示例，并未考虑读一半的情况）
 *          取socketChannel，预先设置ByteBuff大小，从socketChannel里面读byteBuffer，返回读取到的字节数
 *          大于0则有值，进行解码 对byteBuffer进行flip()操作，将当前limit设置成position，position设置成0，便于后续对缓冲区的读写操作
 *          设置码流大小的byte[]; byteBuffer.get(bytes);get操作将缓冲区可读字节复制到byte[]数组中
 *
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
                // 未考虑 数据大于byte，，读一半情况
                if(readBytes > 0){
                    buffer.flip();
                    // remaining() 返回读取码流大小  Returns the number of elements between the current position and the* limit.
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

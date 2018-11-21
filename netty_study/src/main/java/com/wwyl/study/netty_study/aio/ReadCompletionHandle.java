package com.wwyl.study.netty_study.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/11/21 17:05
 * @Description:
 */
public class ReadCompletionHandle implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandle(AsynchronousSocketChannel channel) {
        if(channel != null){
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try{
            String reqs = new String(body,"utf-8");
            System.out.println("the time server recieved order is " + reqs);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(reqs)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
            System.out.println("The time server send currentTime:" + currentTime);
            doWrite(currentTime);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void doWrite(String msg) {
        if(msg != null && !"".equals(msg.trim())){
            try {
                byte[] bytes = msg.getBytes("UTF-8");
                ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
                byteBuffer.put(bytes);
                byteBuffer.flip();
                channel.write(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                    @Override
                    public void completed(Integer result, ByteBuffer attachment) {
                        // 如果没发完 继续发
                        if(attachment.hasRemaining()){
                            channel.write(byteBuffer,byteBuffer,this);
                        }
                    }

                    @Override
                    public void failed(Throwable exc, ByteBuffer attachment) {
                        try{
                            channel.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try{
            this.channel.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

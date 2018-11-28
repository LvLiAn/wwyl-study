package com.wwyl.study.netty_study.netty01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Auther: lvla
 * @Date: 2018/11/28 15:25
 * @Description:
 */
public class TimeClinetHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端连接时，发送信息给服务端
        byte[] clientReq = "QUERY TIME ORDER".getBytes();
        ByteBuf clientReqBuf = Unpooled.copiedBuffer(clientReq);
        ctx.writeAndFlush(clientReqBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收服务端返回信息
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        String body = new String(bytes,"utf-8");
        System.out.println("the netty client recieve nettyServer recieve msg is " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

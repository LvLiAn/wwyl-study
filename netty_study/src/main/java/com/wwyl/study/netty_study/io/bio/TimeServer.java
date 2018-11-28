package com.wwyl.study.netty_study.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Auther: lvla
 * @Date: 2018/11/16 14:07
 * @Description:
 *   1、BIO模式（同步阻塞模型，一客户端一线程），接收客户端信息，，如果没有客户端接入，主程序会在accept()方法处阻塞，
 *      并且处理客户端信息时，每次会new一个线程去处理，一个线程只会处理一个客户端连接，无法满足高并发高性能场景
 *
 *   2、伪异步IO（基于BIO模式进行的改进，一个连接使用线程池线程来处理，达到线程复用，防止线程创建过多，资源耗尽），
 *      使用伪异步IO模式，虽然解决一个客户端一个线程的问题，但是在socket传输过程中，会一直占有线程，直到传输完毕，
 *      或者发生异常，这就导致在高并发的情况下，线程会处于队列当中排队，甚至是线程池满了，导致异常
 *
 *      BIO模式下，不适用高并发高性能场景
 */
public class TimeServer {

    public static void main(String[] args) throws IOException {
        // 默认端口8080
        int port = 8080;
        ServerSocket serverSocket = null;
        try{
            System.out.println(Runtime.getRuntime().availableProcessors());
            serverSocket = new ServerSocket(port);
            System.out.println("The time server is start port is:" + port);
            Socket socket = null;
            //2、伪异步IO，
            TimeServerHandlerExecPool singlePool = new TimeServerHandlerExecPool(4,2);
            while (true){
                socket = serverSocket.accept();
                //1、BIO模式 将服务端接收到的socket 新启子线程处理
                // new Thread(new TimeServerHandler(socket)).start();

                //2、伪异步IO，使用线程池来处理接收到的客户端
                singlePool.exec(new TimeServerHandler(socket));

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(serverSocket != null){
                System.out.println("The time server is close!");
                serverSocket.close();
            }
        }

    }
}

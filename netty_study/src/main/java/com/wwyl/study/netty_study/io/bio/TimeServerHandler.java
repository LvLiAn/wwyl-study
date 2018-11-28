package com.wwyl.study.netty_study.io.bio;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * @Auther: lvla
 * @Date: 2018/11/16 14:13
 * @Description:
 */
public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(),true);
            String body = null;
            String currentTime = null;
            while (true){
                body = reader.readLine();
                if(StringUtils.isEmpty(body)){
                    System.out.println("传递body为空！");
                    break;
                }
                System.out.println("The time server recieve Order is:" + body);
                currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)? new Date(System.currentTimeMillis()).toString():"BAD ORDER";
                System.out.println("The time server send currentTime:" + currentTime);
                writer.println(currentTime);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(reader != null) {
                    reader.close();
                }
                if(writer != null){
                    writer.close();
                }
                if(this.socket != null){
                    this.socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

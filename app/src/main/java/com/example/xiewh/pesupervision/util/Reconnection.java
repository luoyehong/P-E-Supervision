package com.example.xiewh.pesupervision.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;

import com.example.xiewh.pesupervision.LoginActivity;
import com.example.xiewh.pesupervision.MainActivity;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by XiEwH on 2017/8/9.
 */

public class Reconnection implements Runnable {

    Reconnection myThread;
    private String ip,port;
    private Thread thread;
    private String address;


    public void reconnection(Context context,String address){
        System.out.println("运行重连程序");
        SharedPreferences pref = context.getSharedPreferences("ip_port", Context.MODE_PRIVATE);
        ip=pref.getString("ip","");
        port=pref.getString("port","");

        myThread = new Reconnection();
        myThread.setIP_Port(ip,port,address);
        thread = new Thread(myThread);
        thread.start();
    }



    @Override
    public void run() {
        Socket socket = new Socket();
        int timeoutCount=0;
        Message message = new Message();
        while (true){
            try {
                Thread.sleep(500);
                System.out.println("建立连接中...IP:"+ ip +",端口："+port);
                socket = new Socket();
                socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)),1000);
                socket.setSoTimeout(1000);
                System.out.println("TCP连接成功...");
                message.what=2;
                socket.close();
                Thread.sleep(500);
                break;
            }catch (Exception e){
                timeoutCount++;
                System.out.println("TCP连接失败："+e);
                if (timeoutCount==10){
                    message.what=3;
                    break;
                }
            }
        }
        if ("login".equals(address))
        {
            LoginActivity.handlerUpdate.handleMessage(message);
        }else if ("main".equals(address)) {
            MainActivity.reconHandler.handleMessage(message);
        }
    }


    private void setIP_Port(String ip,String port,String address){
        this.ip = ip;
        this.port = port;
        this.address = address;
    }
}

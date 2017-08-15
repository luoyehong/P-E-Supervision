package com.example.xiewh.pesupervision.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.view.View;

import com.example.xiewh.pesupervision.MainActivity;
import com.example.xiewh.pesupervision.coolingFragment;
import com.example.xiewh.pesupervision.enviFragment;
import com.example.xiewh.pesupervision.service.AutoUpdateService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by XiEwH on 2017/8/7.
 */

public class WriteData implements Runnable {

    WriteData myThread;
    private String ip,port,mes;
    private ProgressDialog progressDialog;
    private Thread thread;
    private String address;
    private View view;

    public void writeData(View view, String message, String address){
        Intent intent = new Intent(view.getContext(),AutoUpdateService.class);//描述跳转服务的intent  
        view.getContext().stopService(intent);
        SharedPreferences pref = view.getContext().getSharedPreferences("ip_port", Context.MODE_PRIVATE);
        ip=pref.getString("ip","");
        port=pref.getString("port","");
        this.view=view;
        showProgressDialog();
        myThread = new WriteData();
        myThread.setIP_Port_Message(ip,port,message,address,progressDialog,view);
        thread = new Thread(myThread);
        thread.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500);
        }catch (Exception e){

        }

        Socket socket = new Socket();
        int timeoutCount=0;
        int socketJudge=0;
        Message message = new Message();
        while (socketJudge==0){
            try {
                System.out.println("建立连接中...IP:"+ ip +",端口："+port);
                socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)),1000);
                socket.setSoTimeout(1000);
                System.out.println("TCP连接成功...");
                socketJudge = 1;
            }catch (Exception e){
                timeoutCount++;
                if (timeoutCount==5){
                    message.obj="执行错误：连接超时";
                    break;
                }
                System.out.println("TCP连接失败："+e);
            }
        }
        if (socketJudge==1) {
            try {
                Thread.sleep(200);
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                System.out.println("发送：" + mes);
                out.write(mes);
                out.flush();
                socketJudge=2;
            } catch (Exception e) {
                message.obj = "执行错误：数据传输失败";
            }
        }
        if (socketJudge==2){
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(200);
                    InputStream is = socket.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));
                    if (is.available() != 0) {
                        Thread.sleep(100);
                        System.out.println(is.available());
                        String reply = in.readLine();//读取操作
                        System.out.println("接收：" + reply + mes.substring(0, 3));
                        message.obj=reply;
                        break;
                    }else if (i==9){
                        message.obj = "执行错误：接收不到数据";
                    }
                }
            } catch (Exception e) {
                message.obj = "执行错误：数据接收失败";
                System.out.println(e);
            }
        }
        try {
            socket.close();
        }catch (Exception e)
        {

        }
        closeProgressDialog();

        if (address=="envi"){
            message.what=1;
            enviFragment.handlerUpdate.sendMessage(message);
        }else if (address == "cooling"){
            message.what=1;
            coolingFragment.handlerUpdate.sendMessage(message);
        }
    }

    private void setIP_Port_Message(String ip,String port,String message,String address,ProgressDialog progressDialog,View view){
        this.ip = ip;
        this.port = port;
        this.mes = message;
        this.address = address;
        this.progressDialog = progressDialog;
        this.view=view;
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(view.getContext());
            progressDialog.setMessage("正在执行中...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
            Message message = new Message();
            message.what=4;
            MainActivity.reconHandler.sendMessage(message);
        }
    }
}

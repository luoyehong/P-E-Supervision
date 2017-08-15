package com.example.xiewh.pesupervision.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;

import com.example.xiewh.pesupervision.MainActivity;
import com.example.xiewh.pesupervision.util.Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;


public class AutoUpdateService extends Service implements Runnable{
    private Socket socket;
    private volatile boolean _run;
    private Boolean socketJudge = true;
    AutoUpdateService myThread;
    private Thread thread;
    private SharedPreferences pref;
    private String ip,port;
    private int conCount=0;
    private String mesWrite = null;

    public AutoUpdateService() {
    }

    public void setIP_Port(String ip,String port,Boolean socketJudge)
    {
        this.ip = ip;
        this.port = port;
        this.socketJudge=socketJudge;
    }

    @Override
    public void run() {
        _run=true;
        int i =12;
        while (_run) {
            switch (i) {
                case 12:
                    mesWrite="1,FF";
                    i = 0;
                    break;
                case 8:
                    mesWrite="1,FF";
                    break;
                case 4:
                    mesWrite="1,FF";
                    break;
                default:
                    mesWrite=null;
                    break;
            }
            if (mesWrite!=null) {
                int timeoutCount = 0;
                while (socketJudge && _run) {
                    System.out.println("run：" + _run);
                    try {
                        System.out.println("建立连接中...IP:" + ip + ",端口：" + port);
                        socket = new Socket();
                        socket.connect(new InetSocketAddress(ip, Integer.parseInt(port)), 1000);
                        socket.setSoTimeout(1000);
                        System.out.println("TCP连接成功...");
                        Thread.sleep(100);
                        socketJudge = false;
                    } catch (Exception e) {
                        System.out.println("TCP连接失败：" + e);
                        timeoutCount++;
                        if (timeoutCount == 5) {
                            _run = false;
                            Message msg = new Message();
                            msg.what = 1;
                            MainActivity.handler.sendMessage(msg);
                            break;
                        }
                    }
                }
                if (!socketJudge && _run) {
                    try {
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        System.out.println("发送：" + mesWrite);
                        out.write(mesWrite);
                        out.flush();
                    } catch (Exception e) {
                        System.out.println("Socket发送故障：" + e);
                    }
                    //发送给服务端的消息
                }
            }else {
                if (_run) {
                    try {
                        conCount++;
                        System.out.println(conCount);
                        InputStream is = socket.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(is));
                        DataInputStream input = new DataInputStream(is);
                        if (is.available() != 0) {
                            Thread.sleep(100);
                            System.out.println(is.available());

                            String reply = input.readUTF();//读取操作
                            conCount=0;
                            System.out.println("接收：" + reply);
                            Utility.handleWeatherResponse(reply);
                            Message msg = new Message();
                            MainActivity.handler.sendMessage(msg);
                        }
                        else if (conCount == 36){
                            _run=false;
                            Message msg = new Message();
                            msg.what=1;
                            MainActivity.handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        System.out.println("Socket读取故障：" + e);
                    }
                }
            }
            try {
                if (_run) {
                    i++;
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        try {
            Thread.sleep(100);
            socket.close();
            System.out.println("TCP连接关闭");
        } catch (Exception e) {
            System.out.println("TCP连接关闭故障：" + e);
        }

    }


    @Override
    public void onCreate() {
        pref = getApplicationContext().getSharedPreferences("ip_port", Context.MODE_PRIVATE);
        ip=pref.getString("ip","");
        port=pref.getString("port","");
        myThread = new AutoUpdateService();
        myThread.setIP_Port(ip,port,socketJudge);
        thread = new Thread(myThread);
        thread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myThread._run=false;
        System.out.println("onDe："+_run);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
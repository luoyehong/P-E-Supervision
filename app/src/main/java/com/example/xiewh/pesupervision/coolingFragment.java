package com.example.xiewh.pesupervision;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.czp.library.ArcProgress;
import com.example.xiewh.pesupervision.db.StatusData;
import com.example.xiewh.pesupervision.util.WriteData;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XiEwH on 2017/7/24.
 */

public class coolingFragment extends Fragment {
    public volatile boolean longClicked = false;
    public volatile boolean threadFlag = false;
    public static Handler handlerUpdate;
    private ArcProgress mProgress;
    private TextView ACstate,ACswitchText,supplyTemp,returnTemp,ACFanStall,AcRefrigeration,emergencyFan;
    private boolean mfinis =false;
    private WriteData writeData=new WriteData();
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cooling_fragment,container,false);
        initLayout(view);
        longClick();
        dataUpdate();
        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    switchAC(msg.obj.toString());
                }else {
                    dataUpdate();
                }
            }
        };
        return view;
    }

    private void dataUpdate(){
        List<StatusData> statusData = DataSupport.findAll(StatusData.class);
        if (statusData.size() == 57) {
            switch (statusData.get(21).getValue()) {
                case 0:
                    ACstate.setText("空调状态：本地关机");
                    ACswitchText.setText("开启");
                    mProgress.setProgress(0);
                    break;
                case 1:
                    ACstate.setText("空调状态：远程关机");
                    ACswitchText.setText("开启");
                    mProgress.setProgress(0);
                    break;
                case 2:
                    ACstate.setText("空调状态：监控关机");
                    ACswitchText.setText("开启");
                    mProgress.setProgress(0);
                    break;
                case 3:
                    ACstate.setText("空调状态：组网待机");
                    ACswitchText.setText("关闭");
                    mProgress.setProgress(100);
                    break;
                case 4:
                    ACstate.setText("空调状态：电源保护");
                    ACswitchText.setText("关闭");
                    mProgress.setProgress(100);
                    break;
                case 5:
                    ACstate.setText("空调状态：气流保护");
                    ACswitchText.setText("关闭");
                    mProgress.setProgress(100);
                    break;
                case 6:
                    ACstate.setText("空调状态：压力锁定");
                    ACswitchText.setText("关闭");
                    mProgress.setProgress(100);
                    break;
                case 7:
                    ACstate.setText("空调状态：系统开机");
                    ACswitchText.setText("关闭");
                    mProgress.setProgress(100);
                    break;
                default:
                    break;
            }

            double value = statusData.get(14).getValue();
            supplyTemp.setText(String.valueOf(value / 10) + "℃");
            value = statusData.get(9).getValue();
            returnTemp.setText(String.valueOf(value / 10) + "℃");

            switch (statusData.get(26).getValue()) {
                case 0:
                    ACFanStall.setText("关闭");
                    break;
                case 1:
                    ACFanStall.setText("一档位");
                    break;
                case 2:
                    ACFanStall.setText("二档位");
                    break;
                case 3:
                    ACFanStall.setText("三档位");
                    break;
                default:
                    break;
            }
            switch (statusData.get(8).getValue()) {
                case 0:
                    AcRefrigeration.setText("关闭");
                    break;
                case 1:
                    AcRefrigeration.setText("启动");
                    break;
                default:
                    break;
            }

            String state = Integer.toBinaryString(statusData.get(56).getValue());
            if ("0".equals(state.substring(state.length()-1,state.length()))) {
                emergencyFan.setText("关闭");
            }else if ("1".equals(state.substring(state.length()-1,state.length()))){
                emergencyFan.setText("启动");
            }
        }
    }

    private void switchAC(String msg){
        if (msg.indexOf("执行错误")!=-1){
            Toast.makeText(view.getContext(),msg,Toast.LENGTH_LONG).show();
            if ("开启".equals(ACswitchText.getText())) {
                mProgress.setProgress(0);
            }else if("关闭".equals(ACswitchText.getText())) {
                mProgress.setProgress(100);
            }
        }else {
            if ("7,1".equals(msg)) {
                if ("开启".equals(ACswitchText.getText())) {
                    Toast.makeText(view.getContext(),"空调已打开",Toast.LENGTH_LONG).show();
                    ACswitchText.setText("关闭");
                }else if("关闭".equals(ACswitchText.getText())) {
                    Toast.makeText(view.getContext(),"空调已关闭",Toast.LENGTH_LONG).show();
                    ACswitchText.setText("开启");
                }
            } else {
                Toast.makeText(view.getContext(),"执行错误：数据执行失败",Toast.LENGTH_LONG).show();
                if ("开启".equals(ACswitchText.getText())) {
                    mProgress.setProgress(0);
                }else if("关闭".equals(ACswitchText.getText())) {
                    mProgress.setProgress(100);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mfinis = true;
    }

    class OnAirConditionerThread implements Runnable{
        private ArcProgress progressBar;
        private int i =0;
        public OnAirConditionerThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for(;i<=100&&i>=0;i++){
                if(mfinis){
                    break;
                }else if (longClicked)
                {
                    i=i-2;
                }
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = i;
                msg.obj = progressBar;
                SystemClock.sleep(30);
                handler.sendMessage(msg);
            }
            threadFlag=false;
        }
    }

    class OffAirConditionerThread implements Runnable{
        private ArcProgress progressBar;
        private int i =100;
        public OffAirConditionerThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for(;i<=100&&i>=0;i--){
                if(mfinis){
                    break;
                }else if (longClicked)
                {
                    i=i+2;
                }
                Message msg = new Message();
                msg.what = 2;
                msg.arg1 = i;
                msg.obj = progressBar;
                SystemClock.sleep(30);
                handler.sendMessage(msg);
            }
            threadFlag=false;
        }
    }

    private void initLayout(View view){
        mProgress = (ArcProgress) view.findViewById(R.id.AC_switch);
        ACstate = (TextView) view.findViewById(R.id.cooling_ACstate);
        ACswitchText = (TextView) view.findViewById(R.id.cooling_buttonText);
        supplyTemp = (TextView) view.findViewById(R.id.cooling_supplyTemp);
        returnTemp = (TextView) view.findViewById(R.id.cooling_returnTemp);
        ACFanStall = (TextView) view.findViewById(R.id.cooling_ACFanStall);
        AcRefrigeration = (TextView) view.findViewById(R.id.cooling_AcRefrigeration);
        emergencyFan = (TextView) view.findViewById(R.id.cooling_emergencyFan);
    }

    private void longClick(){
        mProgress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClicked = false;
                System.out.println(threadFlag);
                if(!threadFlag) {
                    System.out.println(ACswitchText.getText());
                    if ("开启".equals(ACswitchText.getText())) {
                        System.out.println(ACswitchText.getText());
                        Thread thread = new Thread(new OnAirConditionerThread(mProgress));
                        thread.start();
                        threadFlag = true;
                    }else if ("关闭".equals(ACswitchText.getText())){
                        Thread thread = new Thread(new OffAirConditionerThread(mProgress));
                        thread.start();
                        threadFlag = true;
                    }
                }
                return false;
            }
        });
        mProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    longClicked = true;
                }
                return false;
            }
        });
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArcProgress progressBar = (ArcProgress) msg.obj;
            if (msg.what==1) {
                switch (msg.arg1) {
                    case 100:
                        progressBar.setProgress(msg.arg1);
                        writeData.writeData(view,"7,1,86,0","cooling"); //空调远程开机
                        break;
                    default:
                        progressBar.setProgress(msg.arg1);
                        break;
                }
            }else if (msg.what == 2){
                switch (msg.arg1) {
                    case 0:
                        progressBar.setProgress(msg.arg1);
                        writeData.writeData(view,"7,1,86,0","cooling"); //空调远程关机
                        break;
                    default:
                        progressBar.setProgress(msg.arg1);
                        break;
                }
            }
        }
    };
}

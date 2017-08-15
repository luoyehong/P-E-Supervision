package com.example.xiewh.pesupervision;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class enviFragment extends Fragment {

    public volatile boolean frontLongClicked = false;
    public volatile boolean frontThreadFlag = false;
    public volatile boolean BackLongClicked = false;
    public volatile boolean BackThreadFlag = false;
    public static Handler handlerUpdate;
    private View hotAisle,coldAisle;
    private ArcProgress frontDoorSwitch,backDoorSwitch;
    private TextView frontDoorState,backDoorState,leakState,frontTemp,frontHumidity,rearTemp,rearHumidity;
    private boolean mfinis =false;
    private WriteData writeData=new WriteData();
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.envi_fragment,container,false);
        initLayout(view);
        onClick();
        dataUpdate();
        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    if (msg.obj.toString().indexOf("执行错误")!=-1){
                        Toast.makeText(view.getContext(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    }else {
                        if ("7,1".equals(msg.obj.toString())) {
                            Toast.makeText(view.getContext(),"门已打开",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(view.getContext(),"执行错误：数据执行失败",Toast.LENGTH_LONG).show();
                        }
                    }

                }else {
                    dataUpdate();
                }
            }
        };
        return view;
    }

    private void dataUpdate(){
        List<StatusData> statusData = DataSupport.findAll(StatusData.class);
        Log.d("dataUpdate", "数据行数: " + statusData.size());
        if (statusData.size() == 57) {
            String state = Integer.toBinaryString(statusData.get(55).getValue());
            if ("0".equals(state.substring(state.length()-1,state.length()))) {
                frontDoorState.setText("前门状态：闭合");
            }else if ("1".equals(state.substring(state.length()-1,state.length()))){
                frontDoorState.setText("前门状态：开启");
            }
            if (state.length()<2){
                backDoorState.setText("后门状态：闭合");
            }else {
                if ("0".equals(state.substring(state.length() - 2, state.length() -1))) {
                    backDoorState.setText("后门状态：闭合");
                } else if ("1".equals(state.substring(state.length() - 2, state.length()- 1))) {
                    backDoorState.setText("后门状态：开启");
                }
            }
            if(state.length()<6){
                leakState.setText("漏水状态：正常");
            }else {
                if ("0".equals(state.substring(state.length() - 6, state.length()-5))) {
                    leakState.setText("漏水状态：正常");
                } else if ("1".equals(state.substring(state.length() - 6, state.length()-5))) {
                    leakState.setText("漏水状态：异常");
                }
            }

            double value = statusData.get(27).getValue();
            frontTemp.setText(String.valueOf(value / 10));
            value = statusData.get(28).getValue();
            frontHumidity.setText(String.valueOf(value / 10));
            value = statusData.get(31).getValue();
            rearTemp.setText(String.valueOf(value / 10));
            value = statusData.get(32).getValue();
            rearHumidity.setText(String.valueOf(value / 10));
        }
    }

    private void initLayout(View view){
        coldAisle = view.findViewById(R.id.coldAisle_button);
        hotAisle = view.findViewById(R.id.hotAisle_button);
        frontDoorSwitch = (ArcProgress) view.findViewById(R.id.FrontDoor_switch);
        backDoorSwitch = (ArcProgress) view.findViewById(R.id.BackDoor_switch);
        frontDoorState = (TextView) view.findViewById(R.id.envi_frontDoorState);
        backDoorState = (TextView) view.findViewById(R.id.envi_backDoorState);
        leakState = (TextView) view.findViewById(R.id.envi_leakState);
        frontTemp = (TextView) view.findViewById(R.id.envi_frontTemp);
        frontHumidity = (TextView) view.findViewById(R.id.envi_frontHumidity);
        rearTemp = (TextView) view.findViewById(R.id.envi_rearTemp);
        rearHumidity = (TextView) view.findViewById(R.id.envi_rearHumidity);
    }

    private void onClick(){
        coldAisle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SubActivity.class);
                intent.putExtra(SubActivity.MAIN_LEVEL,2);
                v.getContext().startActivity(intent);
            }
        });

        hotAisle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SubActivity.class);
                intent.putExtra(SubActivity.MAIN_LEVEL,3);
                v.getContext().startActivity(intent);
            }
        });

        frontDoorSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                frontLongClicked = false;
                if(!frontThreadFlag) {
                    Thread thread = new Thread(new OnFrontDoorThread(frontDoorSwitch));
                    thread.start();
                    frontThreadFlag = true;
                }
                return false;
            }
        });
        frontDoorSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    frontLongClicked = true;
                }
                return false;
            }
        });

        backDoorSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BackLongClicked = false;
                if(!BackThreadFlag) {
                    Thread thread = new Thread(new OnBackDoorThread(backDoorSwitch));
                    thread.start();
                    BackThreadFlag = true;
                }
                return false;
            }
        });
        backDoorSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    BackLongClicked = true;
                }
                return false;
            }
        });
    }

    class OnFrontDoorThread implements Runnable{
        private ArcProgress progressBar;
        private int i =0;
        public OnFrontDoorThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for(;i<=100&&i>=0;i++){
                if(mfinis){
                    break;
                }else if (frontLongClicked)
                {
                    i=i-2;
                }
                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = i;
                msg.obj = progressBar;
                SystemClock.sleep(30);
                handler.sendMessage(msg);
                if (i==100){
                    SystemClock.sleep(2000);
                    msg = new Message();
                    msg.what = 1;
                    msg.arg1 = 101;
                    msg.obj = progressBar;
                    handler.sendMessage(msg);
                }
            }
            frontThreadFlag=false;
        }
    }

    class OnBackDoorThread implements Runnable{
        private ArcProgress progressBar;
        private int i =0;
        public OnBackDoorThread(ArcProgress progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            for(;i<=100&&i>=0;i++){
                if(mfinis){
                    break;
                }else if (BackLongClicked)
                {
                    i=i-2;
                }
                Message msg = new Message();
                msg.what = 2;
                msg.arg1 = i;
                msg.obj = progressBar;
                SystemClock.sleep(30);
                handler.sendMessage(msg);
                if (i==100){
                    SystemClock.sleep(2000);
                    msg = new Message();
                    msg.what = 2;
                    msg.arg1 = 101;
                    msg.obj = progressBar;
                    handler.sendMessage(msg);
                }
            }
            BackThreadFlag=false;
        }
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
                        writeData.writeData(view,"7,1,46.0,1","envi"); //开启前门
                        break;
                    case 101:
                        progressBar.setProgress(0);
                        break;
                    default:
                        progressBar.setProgress(msg.arg1);
                        break;
                }
            }else if (msg.what == 2){
                switch (msg.arg1) {
                    case 100:
                        progressBar.setProgress(msg.arg1);
                        writeData.writeData(view,"7,1,46.1,1","envi"); //开启后门
                        break;
                    case 101:
                        progressBar.setProgress(0);
                        break;
                    default:
                        progressBar.setProgress(msg.arg1);
                        break;
                }
            }
        }
    };

}

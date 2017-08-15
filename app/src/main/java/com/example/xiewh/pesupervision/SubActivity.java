package com.example.xiewh.pesupervision;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiewh.pesupervision.db.AlarmData;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SubActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MAIN_LEVEL = "main_Level";

    public static final String MAIN_ID = "main_Id";

    private List<AlarmData> alarmDataList;

    private ScrollView subAlert,subEnvi,subTCPSet;

    private TextView subTitle;

    private TextView alarmEvent,alarmTime,alarmState,alarmMakeSurePeople,alarmDefiniteTime,alarmEndTime;

    private Button butBack,TCPSetmodify;

    private EditText TCPIP,TCPPort;

    private Intent intent;

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

//    private Handler mHandle = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        initLayout();

        intent = getIntent();
        int mainLevel = intent.getIntExtra(MAIN_LEVEL,0);
        if (mainLevel ==1){
            subAlert.setVisibility(View.VISIBLE);
            subTitle.setText("报警记录");
            alertInitialization();
        }else if (mainLevel == 2){
            subEnvi.setVisibility(View.VISIBLE);
            subTitle.setText("冷通道状态");
        }else if (mainLevel == 3){
            subEnvi.setVisibility(View.VISIBLE);
            subTitle.setText("热通道状态");
        }else if (mainLevel == 4){
            subTCPSet.setVisibility(View.VISIBLE);
            subTitle.setText("TCP连接设置");
            TCPSetInitialization();
        }

        butBack.setOnClickListener(this);
        TCPSetmodify.setOnClickListener(this);
        TCPIP.setOnClickListener(this);
        TCPPort.setOnClickListener(this);

    }


    private void initLayout(){
        subAlert =(ScrollView) findViewById(R.id.ui_sub_alert);
        subEnvi =(ScrollView) findViewById(R.id.ui_sub_envi);
        subTCPSet =(ScrollView) findViewById(R.id.ui_sub_TCPSet);
        subTitle = (TextView) findViewById(R.id.sub_title_text);
        alarmEvent = (TextView) findViewById(R.id.sub_alert_event);
        alarmTime = (TextView) findViewById(R.id.sub_alert_time);
        alarmState = (TextView) findViewById(R.id.sub_alert_state);
        alarmMakeSurePeople = (TextView) findViewById(R.id.sub_alert_people);
        alarmDefiniteTime = (TextView) findViewById(R.id.sub_alert_definiteTime);
        alarmEndTime = (TextView) findViewById(R.id.sub_alert_endTime);
        butBack = (Button) findViewById(R.id.sub_back_button);
        TCPSetmodify = (Button) findViewById(R.id.sub_TCPSet_modify);
        TCPIP = (EditText) findViewById(R.id.sub_TCPSet_ip);
        TCPPort = (EditText) findViewById(R.id.sub_TCPSet_port);
    }

    private void alertInitialization(){
        int mainId = intent.getIntExtra(MAIN_ID,0);
        alarmDataList = DataSupport.where("id = ?",String.valueOf(mainId)).find(AlarmData.class);
        if (alarmDataList.size()>0){
            alarmEvent.setText(alarmDataList.get(0).getAlarmEvent());
            alarmTime.setText(alarmDataList.get(0).getAlarmTime());
            alarmState.setText(alarmDataList.get(0).getAlarmState());
            alarmMakeSurePeople.setText(alarmDataList.get(0).getAlarmMakeSurePeople());
            alarmDefiniteTime.setText(alarmDataList.get(0).getAlarmDefiniteTime());
            alarmEndTime.setText(alarmDataList.get(0).getAlarmEndTime());
            if ("未确认".equals(alarmState.getText())){
                alarmState.setTextColor(Color.rgb(255,44,44));

            }else if ("已确认".equals(alarmState.getText())){
                alarmState.setTextColor(Color.rgb(255,123,44));
            }
        }
    }

    private void  TCPSetInitialization(){
        pref= getApplicationContext().getSharedPreferences("ip_port",Context.MODE_PRIVATE);
        if (pref.getString("ip","NULL")=="NULL") {
            editor = pref.edit();
            editor.putString("ip", "192.168.4.1");
            editor.putString("port", "1001");
            editor.apply();
            TCPIP.setText("192.168.4.1");
            TCPPort.setText("1001");
        }else {
            String ip = pref.getString("ip", "");
            String port = pref.getString("port", "");
            TCPIP.setText(ip);
            TCPPort.setText(port);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sub_back_button:
                intent = new Intent(SubActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sub_TCPSet_modify:
                String ip = TCPIP.getText().toString();
                String port = TCPPort.getText().toString();
                if (ip.length()>0&&port.length()>0){
                    editor = pref.edit();
                    editor.putString("ip",ip);
                    editor.putString("port",port);
                    editor.apply();
                    intent = new Intent(SubActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    Toast.makeText(SubActivity.this, "Service IP和端口不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.sub_TCPSet_ip:
//                mHandle.postDelayed(new Runnable(){
//                    @Override
//                    public void run(){
//                        //将ScrollView滚动到底  
//                        subTCPSet.fullScroll(View.FOCUS_DOWN);
//                    }
//                }, 200);
//                break;
//            case R.id.sub_TCPSet_port:
//                mHandle.postDelayed(new Runnable(){
//                    @Override
//                    public void run(){
//                        //将ScrollView滚动到底  
//                        subTCPSet.fullScroll(View.FOCUS_DOWN);
//                    }
//                }, 200);
//                break;
            default:
                break;
        }

    }
}

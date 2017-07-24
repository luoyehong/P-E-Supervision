package com.example.xiewh.pesupervision;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xiewh.pesupervision.db.AlarmData;

import org.litepal.crud.DataSupport;

import java.util.List;

public class SubActivity extends AppCompatActivity {

    public static final String MAIN_LEVEL = "main_Level";

    public static final String MAIN_ID = "main_Id";

    private List<AlarmData> alarmDataList;

    private View subAlert,subEnvi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        subAlert = findViewById(R.id.ui_sub_alert);
        subEnvi = findViewById(R.id.ui_sub_envi);
        TextView alarmEvent = (TextView) findViewById(R.id.sub_alert_event);
        TextView alarmTime = (TextView) findViewById(R.id.sub_alert_time);
        TextView alarmState = (TextView) findViewById(R.id.sub_alert_state);
        TextView alarmMakeSurePeople = (TextView) findViewById(R.id.sub_alert_people);
        TextView alarmDefiniteTime = (TextView) findViewById(R.id.sub_alert_definiteTime);
        TextView alarmEndTime = (TextView) findViewById(R.id.sub_alert_endTime);
        Button butBack = (Button) findViewById(R.id.sub_back_button);
        Intent intent = getIntent();
        int mainLevel = intent.getIntExtra(MAIN_LEVEL,0);
        if (mainLevel ==1){
            subAlert.setVisibility(View.VISIBLE);
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

        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}

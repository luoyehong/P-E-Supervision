package com.example.xiewh.pesupervision;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xiewh.pesupervision.db.AlarmData;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XiEwH on 2017/7/24.
 */

public class recordFragment extends Fragment {


    private List<AlarmData> dataList = new ArrayList<>();

    private List<AlarmData> alarmDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment, container,false);

        queryAlarmData();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.alrm_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(dataList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void queryAlarmData(){
        alarmDatas = DataSupport.where("alarmState = ?","未确认").find(AlarmData.class);
        if (alarmDatas.size()>0){
            for (AlarmData alarmData : alarmDatas){
                dataList.add(alarmData);
            }
        }

        alarmDatas = DataSupport.where("alarmState = ?","已确认").find(AlarmData.class);
        if (alarmDatas.size()>0){
            for (AlarmData alarmData : alarmDatas){
                dataList.add(alarmData);
            }
        }

        alarmDatas = DataSupport.where("alarmState = ?","已结束").find(AlarmData.class);
        if (alarmDatas.size()>0){
            for (AlarmData alarmData : alarmDatas){
                dataList.add(alarmData);
            }
        }

    }


    private void initAlarmData(){
        AlarmData alarm = new AlarmData();
        alarm.setAlarmEvent("前门未关报警");
        alarm.setAlarmTime("2017/07/06  10:24");
        alarm.setAlarmState("未确认");
        alarm.save();
        alarm = new AlarmData();
        alarm.setAlarmEvent("后门未关报警");
        alarm.setAlarmTime("2017/07/06  10:24");
        alarm.setAlarmState("已确认");
        alarm.setAlarmDefiniteTime("2017/07/06  11:24");
        alarm.setAlarmMakeSurePeople("谢委宏");
        alarm.save();
        alarm = new AlarmData();
        alarm.setAlarmEvent("右侧门未关报警");
        alarm.setAlarmTime("2017/07/06  10:24");
        alarm.setAlarmState("已结束");
        alarm.setAlarmEndTime("2017/07/06  11:24");
        alarm.save();
        queryAlarmData();
    }
}

package com.example.xiewh.pesupervision.db;

import org.litepal.crud.DataSupport;

/**
 * Created by XiEwH on 2017/7/15.
 */

public class AlarmData extends DataSupport {

    private int id;

    private String alarmEvent; //警报事件

    private String alarmTime; //警报开始时间

    private String alarmState; //当前状态

    private String alarmMakeSurePeople; //确定人

    private String alarmDefiniteTime; //确定时间

    private String alarmEndTime; //结束时间


    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getAlarmEvent(){
        return alarmEvent;
    }

    public void setAlarmEvent(String alarmEvent){
        this.alarmEvent=alarmEvent;
    }

    public String getAlarmTime(){
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime)
    {
        this.alarmTime=alarmTime;
    }

    public String getAlarmState(){
        return alarmState;
    }

    public void setAlarmState(String alarmState){
        this.alarmState=alarmState;
    }

    public String getAlarmMakeSurePeople(){
        return alarmMakeSurePeople;
    }

    public void setAlarmMakeSurePeople(String alarmMakeSurePeople) {
        this.alarmMakeSurePeople = alarmMakeSurePeople;
    }

    public String getAlarmDefiniteTime() {
        return alarmDefiniteTime;
    }

    public void setAlarmDefiniteTime(String alarmDefiniteTime) {
        this.alarmDefiniteTime = alarmDefiniteTime;
    }

    public String getAlarmEndTime() {
        return alarmEndTime;
    }

    public void setAlarmEndTime(String alarmEndTime) {
        this.alarmEndTime = alarmEndTime;
    }

}

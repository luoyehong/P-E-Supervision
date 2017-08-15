package com.example.xiewh.pesupervision.db;

import org.litepal.crud.DataSupport;

/**
 * Created by XiEwH on 2017/8/11.
 */

public class EventAlarmData extends DataSupport {
    private String number;//序号

    private String alarmEvent; //警报事件

    private String alarmTime; //警报开始时间

    private String alarmState; //当前状态

    private String alarmMakeSurePeople; //确定人

    private String alarmDefiniteTime; //确定时间

    public String getAlarmEvent() {
        return alarmEvent;
    }

    public void setAlarmEvent(String alarmEvent) {
        this.alarmEvent = alarmEvent;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getAlarmMakeSurePeople() {
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

    public String getnumber() {

        return number;
    }

    public void setnumber(String number) {
        this.number = number;
    }
}

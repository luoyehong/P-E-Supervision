package com.example.xiewh.pesupervision.util;

import android.util.Log;

import com.example.xiewh.pesupervision.db.EventAlarmData;
import com.example.xiewh.pesupervision.db.SetData;
import com.example.xiewh.pesupervision.db.StatusData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by XiEwH on 2017/7/29.
 */

public class Utility {

    public static StatusData handleWeatherResponse(String response){

        try {
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getJSONArray("1").length()==57) {
                JSONArray jsonArray = jsonObject.getJSONArray("1");
                if (DataSupport.findAll(StatusData.class).size() >= 57) {
                    DataSupport.deleteAll(StatusData.class);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    StatusData statusData = new StatusData();
                    JSONObject countyObject = jsonArray.getJSONObject(i);
                    statusData.setnumber(String.valueOf(i));
                    statusData.setValue(countyObject.getInt("value"));
                    statusData.save();
                }
            } else if (jsonObject.getJSONObject("2").length()==19){
                JSONArray jsonArray = jsonObject.getJSONArray("2");
                if (DataSupport.findAll(SetData.class).size() >= 57) {
                    DataSupport.deleteAll(SetData.class);
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    SetData setData=new SetData();
                    JSONObject countyObject = jsonArray.getJSONObject(i);
                    setData.setnumber(String.valueOf(i));
                    setData.setValue(countyObject.getInt("value"));
                    setData.save();
                }
            } else if (jsonObject.getJSONObject("3").length()>0){
                JSONArray jsonArray = jsonObject.getJSONArray("3");
                if (DataSupport.findAll(EventAlarmData.class).size() > 0) {
                    StatusData.deleteAll(EventAlarmData.class);
                }
                    for (int i = 0; i < jsonArray.length(); i++) {
                    EventAlarmData eventAlarmData = new EventAlarmData();
                    JSONObject countyObject = jsonArray.getJSONObject(i);
                    eventAlarmData.setnumber(countyObject.getString("ip"));
                    eventAlarmData.setAlarmTime(countyObject.getString("t0"));
                    eventAlarmData.setAlarmEvent(countyObject.getString("event"));
                    eventAlarmData.setAlarmState(countyObject.getString("state"));
                    eventAlarmData.setAlarmMakeSurePeople(countyObject.getString("people"));
                    eventAlarmData.setAlarmDefiniteTime(countyObject.getString("t1"));
                    eventAlarmData.save();
                }
            }
            else
            {
                Log.d("BUG", "handleWeatherResponse:数据格式有问题 ");
            }
        }catch (Exception e){
            Log.d("BUG", "handleWeatherResponse:有问题");
            e.printStackTrace();
        }
        return null;
    }


}

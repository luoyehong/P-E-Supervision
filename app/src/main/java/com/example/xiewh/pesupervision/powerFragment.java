package com.example.xiewh.pesupervision;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiewh.pesupervision.db.StatusData;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XiEwH on 2017/7/28.
 */

public class powerFragment extends Fragment {

    private TextView mainVoltage,mainCurrent,frequency,totalPower,UPSPower,PDUPower,ACPower,UPSVoltage,batteryVoltage,outputCurrent,percentage;
    private ImageView mainImage,SPDImage,ACImage,ACSpareImage,pareImage,UPSImage,UPSOutputImage,bypassImage,LCDImage,PDUImage,pumpImage;
    public static Handler handlerUpdate;
    View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.power_fragment, container,false);
        initLayout(view);
        dataUpdate();

        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dataUpdate();
            }
        };
        return view;
    }

    private void dataUpdate(){
        List<StatusData> statusData = DataSupport.findAll(StatusData.class);
        Log.d("dataUpdate", "数据行数: " + statusData.size());
        if (statusData.size() == 57) {
            String state = Integer.toBinaryString(statusData.get(33).getValue());
            if ("0".equals(state.substring(state.length()-1,state.length()))) {
                mainImage.setImageResource(R.drawable.power_img_off);
            }else if ("1".equals(state.substring(state.length()-1,state.length()))){
                mainImage.setImageResource(R.drawable.power_img_on);
            }
            if (state.length()<2){
                SPDImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 2, state.length()-1))) {
                    SPDImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 2, state.length()-1))) {
                    SPDImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<4){
                ACImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 4, state.length()-3))) {
                    ACImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 4, state.length()-3))) {
                    ACImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<5){
                ACSpareImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 5, state.length()-4))) {
                    ACSpareImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 5, state.length()-4))) {
                    ACSpareImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<6){
                pareImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 6, state.length()-5))) {
                    pareImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 6, state.length()-5))) {
                    pareImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<3){
                UPSImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 3, state.length()-2))) {
                    UPSImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 3, state.length()-2))) {
                    UPSImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<7){
                bypassImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 7, state.length()-6))) {
                    bypassImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 7, state.length()-6))) {
                    bypassImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<8){
                UPSOutputImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 8, state.length()-7))) {
                    UPSOutputImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 8, state.length()-7))) {
                    UPSOutputImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<9){
                LCDImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 9, state.length()-8))) {
                    LCDImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 9, state.length()-8))) {
                    LCDImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<10){
                PDUImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 10, state.length()-8))) {
                    PDUImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 10, state.length()-9))) {
                    PDUImage.setImageResource(R.drawable.power_img_on);
                }
            }
            if(state.length()<11){
                pumpImage.setImageResource(R.drawable.power_img_off);
            }else {
                if ("0".equals(state.substring(state.length() - 11, state.length()-10))) {
                    pumpImage.setImageResource(R.drawable.power_img_off);
                } else if ("1".equals(state.substring(state.length() - 11, state.length()-10))) {
                    pumpImage.setImageResource(R.drawable.power_img_on);
                }
            }
            String string_value = Integer.toHexString(statusData.get(42).getValue())+Integer.toHexString(statusData.get(43).getValue());
            double value = Long.valueOf(string_value,16);
            mainVoltage.setText(String.valueOf(value / 10000) + " V");
            string_value = Integer.toHexString(statusData.get(44).getValue())+Integer.toHexString(statusData.get(45).getValue());
            value = Long.valueOf(string_value,16);
            mainCurrent.setText(String.valueOf(value / 10000) + " A");
            value = statusData.get(54).getValue();
            frequency.setText(String.valueOf(value / 100) + " Hz");
            string_value = Integer.toHexString(statusData.get(46).getValue())+Integer.toHexString(statusData.get(47).getValue());
            value = Long.valueOf(string_value,16);
            totalPower.setText(String.valueOf(value / 10000) + " KW");
            string_value = Integer.toHexString(statusData.get(48).getValue())+Integer.toHexString(statusData.get(49).getValue());
            value = Long.valueOf(string_value,16);
            UPSPower.setText(String.valueOf(value / 10000) + " KW");
            string_value = Integer.toHexString(statusData.get(50).getValue())+Integer.toHexString(statusData.get(51).getValue());
            value = Long.valueOf(string_value,16);
            ACPower.setText(String.valueOf(value / 10000) + " KW");
            string_value = Integer.toHexString(statusData.get(52).getValue())+Integer.toHexString(statusData.get(53).getValue());
            value = Long.valueOf(string_value,16);
            PDUPower.setText(String.valueOf(value / 10000) + " KW");
            value = statusData.get(2).getValue();
            UPSVoltage.setText(String.valueOf(value / 10) + " V");
            value = statusData.get(6).getValue();
            batteryVoltage.setText(String.valueOf(value / 10) + " V");
            value = statusData.get(4).getValue();
            outputCurrent.setText(String.valueOf(value) + " A");
            value = statusData.get(5).getValue();
            percentage.setText(String.valueOf(value) + " %");
        }
    }

    private void initLayout(View view){
        mainVoltage = (TextView) view.findViewById(R.id.power_mainVoltage);
        mainCurrent = (TextView) view.findViewById(R.id.power_mainCurrent);
        frequency = (TextView) view.findViewById(R.id.power_frequency);
        totalPower = (TextView) view.findViewById(R.id.power_totalPower);
        UPSPower = (TextView) view.findViewById(R.id.power_UPSPower);
        PDUPower = (TextView) view.findViewById(R.id.power_PDUPower);
        ACPower = (TextView) view.findViewById(R.id.power_ACPower);
        UPSVoltage = (TextView) view.findViewById(R.id.power_UPSVoltage);
        batteryVoltage = (TextView) view.findViewById(R.id.power_batteryVoltage);
        outputCurrent = (TextView) view.findViewById(R.id.power_outputCurrent);
        percentage = (TextView) view.findViewById(R.id.power_percentage);
        mainImage = (ImageView) view.findViewById(R.id.power_mainImage);
        SPDImage = (ImageView) view.findViewById(R.id.power_SPDImage);
        ACImage = (ImageView) view.findViewById(R.id.power_ACImage);
        ACSpareImage = (ImageView) view.findViewById(R.id.power_ACSpareImage);
        pareImage = (ImageView) view.findViewById(R.id.power_pareImage);
        UPSImage = (ImageView) view.findViewById(R.id.power_UPSImage);
        UPSOutputImage = (ImageView) view.findViewById(R.id.power_UPSOutputImage);
        bypassImage = (ImageView) view.findViewById(R.id.power_bypassImage);
        LCDImage = (ImageView) view.findViewById(R.id.power_LCDImage);
        PDUImage = (ImageView) view.findViewById(R.id.power_PDUImage);
        pumpImage = (ImageView) view.findViewById(R.id.power_pumpImage);
    }
}

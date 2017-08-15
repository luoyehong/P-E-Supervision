package com.example.xiewh.pesupervision;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiewh.pesupervision.db.StatusData;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by XiEwH on 2017/7/24.
 */

public class homeFragment extends Fragment {

    private TextView outerTemperature,frontTemp,rearTemp,supplyTemp,returnTemp,ACFanStall,AcRefrigeration,UPSwork,UPSload;
    public static Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.home_fragment,container,false);
        initLayout(view);
        dataUpdate();


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dataUpdate();
            }
        };
        return view;
    }

    private void dataUpdate(){
        List<StatusData> statusData = DataSupport.findAll(StatusData.class);

        if (statusData.size() == 57) {
            double value = statusData.get(29).getValue();
            outerTemperature.setText("柜外温度："+String.valueOf(value / 10) +"℃");
            value = statusData.get(27).getValue();
            frontTemp.setText(String.valueOf(value / 10) + "℃");
            value = statusData.get(31).getValue();
            rearTemp.setText(String.valueOf(value / 10) + "℃");
            value = statusData.get(14).getValue();
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
            //UPSwork = (TextView) findViewById(R.id.home_UPSload);

            UPSload.setText("负载：" + String.valueOf(statusData.get(5).getValue()) + "%");
        }
    }

    private void initLayout(View view){
        outerTemperature = (TextView) view.findViewById(R.id.home_outerTemperature);
        frontTemp = (TextView) view.findViewById(R.id.home_frontTemp);
        rearTemp = (TextView) view.findViewById(R.id.home_rearTemp);
        supplyTemp = (TextView) view.findViewById(R.id.home_supplyTemp);
        returnTemp = (TextView) view.findViewById(R.id.home_returnTemp);
        ACFanStall = (TextView) view.findViewById(R.id.home_ACFanStall);
        AcRefrigeration = (TextView) view.findViewById(R.id.home_AcRefrigeration);
        UPSload = (TextView) view.findViewById(R.id.home_UPSload);
        UPSwork = (TextView) view.findViewById(R.id.home_UPSwork);
    }
}

package com.example.xiewh.pesupervision;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiewh.pesupervision.db.AlarmData;

import java.util.List;

/**
 * Created by XiEwH on 2017/7/24.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<AlarmData> mAlramList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View alarmView;
        TextView alarmEventText;
        TextView alarmStateText;
        TextView alarmTimeText;
        TextView timeText;

        public ViewHolder(View view){
            super(view);
            alarmView = view;
            alarmEventText= (TextView) view.findViewById(R.id.alarmEvent_text_item);
            alarmStateText = (TextView) view.findViewById(R.id.alarmState_text_item);
            alarmTimeText = (TextView) view.findViewById(R.id.alarmTime_text_item);
            timeText= (TextView) view.findViewById(R.id.time_text_item);
        }
    }

    public FruitAdapter(List<AlarmData> AlramList){
        mAlramList = AlramList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item , parent , false);
        final ViewHolder holder = new ViewHolder(view);
        holder.alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmData alarmData = mAlramList.get(holder.getAdapterPosition());
                Intent intent = new Intent(v.getContext(), SubActivity.class);
                intent.putExtra(SubActivity.MAIN_ID, alarmData.getId());
                intent.putExtra(SubActivity.MAIN_LEVEL,1);
                v.getContext().startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AlarmData alarmData = mAlramList.get(position);
        holder.alarmEventText.setText(alarmData.getAlarmEvent());
        holder.alarmStateText.setText(alarmData.getAlarmState());
        holder.alarmTimeText.setText(alarmData.getAlarmTime());
        if ("未确认".equals(alarmData.getAlarmState())){
            holder.alarmStateText.setTextColor(Color.rgb(255,44,44));
            holder.timeText.setText("-");
        }else if ("已确认".equals(alarmData.getAlarmState())){
            holder.alarmStateText.setTextColor(Color.rgb(255,123,44));
            holder.timeText.setText(alarmData.getAlarmDefiniteTime());
        }else if ("已结束".equals(alarmData.getAlarmState())){
            holder.timeText.setText(alarmData.getAlarmEndTime());
        }
    }

    @Override
    public int getItemCount() {
        return mAlramList.size();
    }
}

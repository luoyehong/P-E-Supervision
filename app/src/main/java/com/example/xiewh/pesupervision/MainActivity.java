package com.example.xiewh.pesupervision;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiewh.pesupervision.db.AlarmData;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout; //用户框
    private TextView toolbarText;
    private RelativeLayout homeLayout,enviLayout,coolingLayout,recordLayout;

    private View uiHome,uiEnvi,uiCooling,uiRecord;

    private List<AlarmData> alarmDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        homeLayout = (RelativeLayout) findViewById(R.id.home_relative);
        enviLayout = (RelativeLayout) findViewById(R.id.envi_relative);
        coolingLayout = (RelativeLayout) findViewById(R.id.cooling_relative);
        recordLayout = (RelativeLayout) findViewById(R.id.record_relative);
        toolbarText = (TextView) findViewById(R.id.toolbar_title);
        uiHome = findViewById(R.id.ui_home);
        uiEnvi = findViewById(R.id.ui_envi);
        uiCooling = findViewById(R.id.ui_cooling);
        uiRecord = findViewById(R.id.ui_record);
        toolbar.setTitle("");//去除本身标题
        setSupportActionBar(toolbar);//加载标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        homeLayout.setOnClickListener(this);
        enviLayout.setOnClickListener(this);
        coolingLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);
        queryAlarmData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.alrm_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FruitAdapter adapter = new FruitAdapter(alarmDatas);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_relative:
                if(uiHome.getVisibility()!=View.VISIBLE) {
                    uiEnvi.setVisibility(View.GONE);
                    uiCooling.setVisibility(View.GONE);
                    uiRecord.setVisibility(View.GONE);
                    uiHome.setVisibility(View.VISIBLE);
                    toolbarText.setText("主页");
                }
                break;
            case R.id.envi_relative:
                if (uiEnvi.getVisibility()!=View.VISIBLE) {
                    uiHome.setVisibility(View.GONE);
                    uiCooling.setVisibility(View.GONE);
                    uiRecord.setVisibility(View.GONE);
                    uiEnvi.setVisibility(View.VISIBLE);
                    toolbarText.setText("环境");
                }
                break;
            case R.id.cooling_relative:
                if (uiCooling.getVisibility()!=View.VISIBLE) {
                    uiHome.setVisibility(View.GONE);
                    uiEnvi.setVisibility(View.GONE);
                    uiRecord.setVisibility(View.GONE);
                    uiCooling.setVisibility(View.VISIBLE);
                    toolbarText.setText("制冷");
                }
                break;
            case R.id.record_relative:
                if (uiRecord.getVisibility()!=View.VISIBLE) {
                    uiHome.setVisibility(View.GONE);
                    uiEnvi.setVisibility(View.GONE);
                    uiCooling.setVisibility(View.GONE);
                    uiRecord.setVisibility(View.VISIBLE);
                    toolbarText.setText("记录");
                }
                break;
            default:
        }
    }

    @Override //把警报图标添加到页首
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.ic_alert:
                Toast.makeText(this, "进入到警报界面", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private void queryAlarmData(){
        alarmDatas = DataSupport.findAll(AlarmData.class);
        if (alarmDatas.size()>0){
            for (AlarmData alarmData : alarmDatas){
                Log.d("queryAlarmData:", alarmData.getId() + " "
                        + alarmData.getAlarmEvent() +" "
                        + alarmData.getAlarmTime() + " "
                        + alarmData.getAlarmState() + ""
                        + alarmData.getAlarmMakeSurePeople() + ""
                        + alarmData.getAlarmDefiniteTime()+ ""
                        + alarmData.getAlarmEndTime());
            }

        }else
        {
            initAlarmData();
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

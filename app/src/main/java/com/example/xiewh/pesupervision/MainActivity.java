package com.example.xiewh.pesupervision;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiewh.pesupervision.db.AlarmData;
import com.example.xiewh.pesupervision.db.EventAlarmData;
import com.example.xiewh.pesupervision.db.SetData;
import com.example.xiewh.pesupervision.db.StatusData;
import com.example.xiewh.pesupervision.service.AutoUpdateService;
import com.example.xiewh.pesupervision.util.Reconnection;

import org.litepal.crud.DataSupport;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Handler handler,reconHandler;
    private DrawerLayout mDrawerLayout; //用户框
    private TextView toolbarText;
    private RelativeLayout homeLayout,enviLayout,powerLayout,coolingLayout,recordLayout;
    private ImageView homeImage,enviImage,powerImage,coolingImage,recordImage;
    private TextView homeText,enviText,powerText,coolingText,recordText;
    private Toolbar toolbar;
    private NavigationView navView;
    private Fragment tab01,tab02,tab03,tab04,tab05;
    private AlertDialog.Builder dialog;
    private ProgressDialog progressDialog;
    private Boolean dialogBoolean = false;
    private Boolean stopBoolean = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLayout();
        setListeners();
        clearDatabase();
        toolbar.setTitle("");//去除本身标题
        setSupportActionBar(toolbar);//加载标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        tab01 = new homeFragment();
        homeText.setTextColor(Color.rgb(26,173,25));
        homeImage.setImageResource(R.drawable.ic_home1);
        transaction.add(R.id.id_content, tab01);
        transaction.commit();
        autoUpdate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopBoolean = false;
        if(!dialogBoolean) {
            System.out.println("启动服务");
            Intent intent = new Intent(MainActivity.this, AutoUpdateService.class);//描述跳转服务的intent  
            startService(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopBoolean = true;
        Intent intent = new Intent(MainActivity.this,AutoUpdateService.class);//描述跳转服务的intent  
        stopService(intent);
    }

    private void clearDatabase(){
        try {
            DataSupport.deleteAll(AlarmData.class);
            DataSupport.deleteAll(StatusData.class);
            DataSupport.deleteAll(EventAlarmData.class);
            DataSupport.deleteAll(SetData.class);
            System.out.println("数据库初始化");
        }catch (Exception e)
        {
            System.out.println("数据库初始化失败："+e);
        }
    }

    private void autoUpdate(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==1){
                    Intent intent = new Intent(MainActivity.this,AutoUpdateService.class);//描述跳转服务的intent  
                    stopService(intent);
                    showProgressDialog();
                    Reconnection reconnection = new Reconnection();
                    reconnection.reconnection(MainActivity.this,"main");
                }else {
                    Message msg1 = new Message();
                    switch (toolbarText.getText().toString()) {
                        case "主页":
                            homeFragment.handler.sendMessage(msg1);
                            break;
                        case "环境":
                            enviFragment.handlerUpdate.sendMessage(msg1);
                            break;
                        case "配电":
                            powerFragment.handlerUpdate.sendMessage(msg1);
                            break;
                        case "制冷":
                            coolingFragment.handlerUpdate.sendMessage(msg1);
                            break;
                        case "记录":

                            break;

                        default:
                            break;
                    }
                }
            }
        };

        reconHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(MainActivity.this,"重连成功",Toast.LENGTH_LONG).show();
                            if (stopBoolean == false) {
//                                System.out.println("启动服务");
//                                Intent intent = new Intent(MainActivity.this, AutoUpdateService.class);//描述跳转服务的intent  
//                                startService(intent);
                            }
                        }
                    });
                }else if (msg.what == 3){
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            dialogBoolean = true;
                            CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
                            builder.setTitle("温馨提示");
                            builder.setMessage("Server端重连失败");
                            builder.setPositiveButton("继续重连", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialogBoolean = false;
                                    System.out.println(dialogBoolean);
                                    showProgressDialog();
                                    Reconnection reconnection = new Reconnection();
                                    reconnection.reconnection(MainActivity.this,"main");
                                }
                            });
                            builder.setNegativeButton("修改Server端", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialogBoolean = false;
                                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                                    intent.putExtra(SubActivity.MAIN_LEVEL,4);
                                    startActivity(intent);
                                }
                            });
                            CustomDialog dialog = builder.create();
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    });
                }else if (msg.what == 4){
                    if (stopBoolean == false) {
//                        Intent intent = new Intent(MainActivity.this, AutoUpdateService.class);//描述跳转服务的intent  
//                        startService(intent);
                    }
                }
            }
        };
    }

    private void initLayout(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        homeLayout = (RelativeLayout) findViewById(R.id.home_relative);
        enviLayout = (RelativeLayout) findViewById(R.id.envi_relative);
        powerLayout = (RelativeLayout) findViewById(R.id.power_relative);
        coolingLayout = (RelativeLayout) findViewById(R.id.cooling_relative);
        recordLayout = (RelativeLayout) findViewById(R.id.record_relative);
        toolbarText = (TextView) findViewById(R.id.toolbar_title);
        homeText = (TextView) findViewById(R.id.ic_text_home);
        enviText = (TextView) findViewById(R.id.ic_text_envi);
        powerText = (TextView) findViewById(R.id.ic_text_power);
        coolingText = (TextView) findViewById(R.id.ic_text_cooling);
        recordText = (TextView) findViewById(R.id.ic_text_record);
        homeImage = (ImageView) findViewById(R.id.ic_image_home);
        enviImage = (ImageView) findViewById(R.id.ic_image_envi);
        powerImage = (ImageView) findViewById(R.id.ic_image_power);
        coolingImage = (ImageView) findViewById(R.id.ic_image_cooling);
        recordImage = (ImageView) findViewById(R.id.ic_image_record);
    }

    private void setListeners(){
        homeLayout.setOnClickListener(this);
        enviLayout.setOnClickListener(this);
        powerLayout.setOnClickListener(this);
        coolingLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println(item.getTitle());
                if ("TCP连接设置".equals(item.getTitle())) {
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    intent.putExtra(SubActivity.MAIN_LEVEL, 4);
                    startActivity(intent);
                }else if ("注销".equals(item.getTitle())){
                    mDrawerLayout.closeDrawers();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            dialogBoolean = true;
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("与Server端连接中断，正在连接中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            dialogBoolean = false;
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_relative:
                setSelect(0);
                break;
            case R.id.envi_relative:
                setSelect(1);
                break;
            case R.id.power_relative:
                setSelect(2);
                break;
            case R.id.cooling_relative:
                setSelect(3);
                break;
            case R.id.record_relative:
                setSelect(4);
                break;
            default:
                break;
        }
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (i) {
            case 0:
                if (toolbarText.getText()!="主页") {
                    icInitialization();
                    tab01 = new homeFragment();
                    homeText.setTextColor(Color.rgb(26,173,25));
                    homeImage.setImageResource(R.drawable.ic_home1);
                    transaction.replace(R.id.id_content, tab01);
                    toolbarText.setText("主页");
                }
                break;
            case 1:
                if (toolbarText.getText()!="环境") {
                    icInitialization();
                    tab02 = new enviFragment();
                    enviText.setTextColor(Color.rgb(26,173,25));
                    enviImage.setImageResource(R.drawable.ic_envi1);
                    transaction.replace(R.id.id_content, tab02);
                    toolbarText.setText("环境");
                }
                break;
            case 2:
                if (toolbarText.getText()!="配电") {
                    icInitialization();
                    tab03 = new powerFragment();
                    powerText.setTextColor(Color.rgb(26,173,25));
                    powerImage.setImageResource(R.drawable.ic_power1);
                    transaction.replace(R.id.id_content, tab03);
                    toolbarText.setText("配电");
                }
                break;
            case 3:
                if (toolbarText.getText()!="制冷") {
                    icInitialization();
                    tab04 = new coolingFragment();
                    coolingText.setTextColor(Color.rgb(26,173,25));
                    coolingImage.setImageResource(R.drawable.ic_cooling1);
                    transaction.replace(R.id.id_content, tab04);
                    toolbarText.setText("制冷");
                }
                break;
            case 4:
                if (toolbarText.getText()!="记录") {
                    icInitialization();
                    tab05 = new recordFragment();
                    recordText.setTextColor(Color.rgb(26,173,25));
                    recordImage.setImageResource(R.drawable.ic_record1);
                    transaction.replace(R.id.id_content, tab05);
                    toolbarText.setText("记录");
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    private void icInitialization(){
        homeText.setTextColor(Color.rgb(0,0,0));
        enviText.setTextColor(Color.rgb(0,0,0));
        coolingText.setTextColor(Color.rgb(0,0,0));
        powerText.setTextColor(Color.rgb(0,0,0));
        recordText.setTextColor(Color.rgb(0,0,0));
        homeImage.setImageResource(R.drawable.ic_home);
        enviImage.setImageResource(R.drawable.ic_envi);
        coolingImage.setImageResource(R.drawable.ic_cooling);
        powerImage.setImageResource(R.drawable.ic_power);
        recordImage.setImageResource(R.drawable.ic_record);
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
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                tab05 = new recordFragment();
                transaction.replace(R.id.id_content, tab05);
                toolbarText.setText("记录");
                transaction.commit();
                break;
            default:
        }
        return true;
    }
}

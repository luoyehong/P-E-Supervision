package com.example.xiewh.pesupervision;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xiewh.pesupervision.util.Reconnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static Handler handlerUpdate;
    private EditText loginUser,loginPassword,loginIP,loginPort;
    private Button loginSign;
    private ProgressDialog progressDialog;
    private String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initLayout();
        initialization();
        handlerReturned();

        loginSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        user = loginUser.getText().toString();
        String password = loginPassword.getText().toString();
        String ip=loginIP.getText().toString();
        String port = loginPort.getText().toString();
        final CustomDialog.Builder builder = new CustomDialog.Builder(LoginActivity.this);

        if (user.length()==0){
            builder.setTitle("温馨提示");
            builder.setMessage("请填写用户名");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
                    }else if (password.length()==0){
            builder.setTitle("温馨提示");
            builder.setMessage("请填写密码");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println(builder.getEditText());

                }
            });
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }else if (ip.length()==0){
            builder.setTitle("温馨提示");
            builder.setMessage("请填写要连接Server的IP地址");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        }else if (port.length()==0){
            builder.setTitle("温馨提示");
            builder.setMessage("请填写要连接Server的端口");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            CustomDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
        } else{
            System.out.println(user+"   "+password);
            if (!"admin".equals(user)){
                builder.setTitle("温馨提示");
                builder.setMessage("用户名不存在。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                CustomDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }else if(!"123456".equals(password)){
                builder.setTitle("温馨提示");
                builder.setMessage("密码输入错误。");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                CustomDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
        }else {
            SharedPreferences pref= getApplicationContext().getSharedPreferences("ip_port", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ip",ip);
            editor.putString("port",port);
            editor.apply();
            showProgressDialog();
            Reconnection reconnection = new Reconnection();
            reconnection.reconnection(LoginActivity.this,"login");
        }
    }
}

    private void handlerReturned(){
        handlerUpdate = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 2){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            Toast.makeText(LoginActivity.this,"连接成功",Toast.LENGTH_LONG).show();
                            SharedPreferences pref= getApplicationContext().getSharedPreferences("PESupervision_User", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user",user);
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else if (msg.what == 3){
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setTitle("温馨提示")
                                    .setMessage("Server连接失败,请检查网络连接。")
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        };
    }

    private void initLayout(){
        loginUser = (EditText) findViewById(R.id.login_user);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginIP = (EditText)findViewById(R.id.login_TCPSet_ip);
        loginPort = (EditText) findViewById(R.id.login_TCPSet_port);
        loginSign = (Button) findViewById(R.id.login_sign);
    }

    private void initialization(){
        SharedPreferences pref= getApplicationContext().getSharedPreferences("PESupervision_User", Context.MODE_PRIVATE);
        if (pref.getString("user","NULL")!="NULL") {
            String user = pref.getString("user", "");
            loginUser.setText(user);
        }
        pref= getApplicationContext().getSharedPreferences("ip_port",Context.MODE_PRIVATE);
        if (pref.getString("ip","NULL")=="NULL") {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ip", "192.168.4.1");
            editor.putString("port", "1001");
            editor.apply();
            loginIP.setText("192.168.4.1");
            loginPort.setText("1001");
        }else {
            String ip = pref.getString("ip", "");
            String port = pref.getString("port", "");
            loginIP.setText(ip);
            loginPort.setText(port);
        }
    }

    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("与Server端连接中...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}

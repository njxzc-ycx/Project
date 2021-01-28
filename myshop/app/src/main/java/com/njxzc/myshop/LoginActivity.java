package com.njxzc.myshop;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText et_userName;
    private EditText et_password;
    private User user = new User();//用于保存用户信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initCompoment();//调用组件初始化方法
    }

    private void initCompoment() {
        et_userName = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText) findViewById(R.id.et_password);
    }

    public void login(View v) throws Exception{
        //1.获取输入的用户名密码
        final String userName = et_userName.getText().toString();
        final String password = et_password.getText().toString();
        if(userName==""||userName.length()==0||userName==null){
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(password==""||password.length()==0||password==null){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url =  "http://192.168.0.103:8088/shopsystem/androidUser/login";
                        OkhttpHelper.login(url, userName,password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseData = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (responseData) {
                                            case "pwdErr":
                                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "locked":
                                                Toast.makeText(LoginActivity.this, "您的账号已被封禁！请联系客服", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "nameErr":
                                                Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "success":
                                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                                break;
                                        }

                                    }
                                });
                                if (responseData.equals("success")) {
                                    try {
                                        toMain();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();
        }
    }

    public void toMain() throws Exception{
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toRegister(View v) throws Exception{
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    //重写onKeyDown方法 点击手机返回键可以回到主界面
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}

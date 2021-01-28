package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_rgUserName;
    private EditText et_rgPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);
        initCompoment();//调用组件初始化方法
    }

    private void initCompoment() {
        et_rgUserName = (EditText) findViewById(R.id.et_rgUserName);
        et_rgPassword = (EditText) findViewById(R.id.et_rgPassword);
    }

    public void register(View v) throws Exception{
        //1.获取输入的用户名密码
        final String userName = et_rgUserName.getText().toString();
        final String password = et_rgPassword.getText().toString();
        if(userName==""||userName.length()==0||userName==null){
            Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else if(password==""||password.length()==0||password==null){
            Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url =  "http://192.168.0.103:8088/shopsystem/androidUser/registerUser";
                        OkhttpHelper.register(url, userName,password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(RegisterActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseData = response.body().string();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        switch (responseData) {
                                            case "userNameExist":
                                                Toast.makeText(RegisterActivity.this, "账号已存在，请重新输入", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "registerSuccess":
                                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "registerFail":
                                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                                break;
                                        }

                                    }
                                });
                                if (responseData.equals("registerSuccess")) {
                                    try {
                                        Login();
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

    private void Login() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toLogin(View v) throws Exception{
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    //重写onKeyDown方法 点击手机返回键可以回到登录界面
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent myIntent = new Intent();
            myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(myIntent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

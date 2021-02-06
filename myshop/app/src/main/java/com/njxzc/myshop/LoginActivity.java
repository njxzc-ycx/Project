package com.njxzc.myshop;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.EditTextDeleteView;
import com.njxzc.myshop.utils.EditTextPwdDeleteView;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditTextDeleteView et_userName;
    private EditTextPwdDeleteView et_password;
    private long exitTime = 0;
    private User user = new User();//用于保存用户信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initCompoment();//调用组件初始化方法
    }

    private void initCompoment() {
        et_userName = (EditTextDeleteView) findViewById(R.id.et_userName);
        et_password = (EditTextPwdDeleteView) findViewById(R.id.et_password);
    }

    public void login(View v) throws Exception{
        //1.获取输入的用户名密码
        final String userName = et_userName.getText().toString();
        final String password = et_password.getText().toString();
        if(userName==""||userName.length()==0||userName==null){
            Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        }else if(password==""||password.length()==0||password==null){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url =  "http://192.168.0.103:8088/shopsystem/androidUser/login2";
                        OkhttpHelper.login(url, userName,password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseData = response.body().string();
                                if(responseData==""){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }else{
                                    try {
                                        final JSONObject user = new JSONObject(responseData);
                                        final User getUser = new User();
                                        getUser.setUserId(user.getInt("userId"));
                                        getUser.setUserName(user.getString("userName"));
                                        getUser.setNickName(user.getString("nickName"));
                                        getUser.setSchool(user.getString("school"));
                                        getUser.setProfilePhoto(user.getString("profilePhoto"));
                                        if(user.getInt("state")==0){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this, "您的账号已被封禁！请联系客服", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(LoginActivity.this, "欢迎"+getUser.getNickName()+"，登录淘校园！", Toast.LENGTH_SHORT).show();
                                                    AppInfo appInfo = (AppInfo) getApplication();
                                                    appInfo.setUser(getUser);//将用户信息保存在AppInfo中
                                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                    startActivity(intent);
                                                    LoginActivity.this.finish();
                                                }
                                            });

                                        }
                                    } catch (JSONException e) {
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



    public void toRegister(View v) throws Exception{
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toCodeLogin(View v) throws Exception{
        Intent intent = new Intent(this,CodeLoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void toForgetPwd(View v) throws Exception{
        Intent intent = new Intent(this,ResetPwdActivity.class);
        startActivity(intent);
        this.finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次返回键退出淘校园", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}

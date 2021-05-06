package com.njxzc.myshop;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessageBody;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.customview.EditTextDeleteView;
import com.njxzc.myshop.customview.EditTextPwdDeleteView;
import com.njxzc.myshop.utils.Model;
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
    private ProgressDialog mProgressDialog;
    private long exitTime = 0;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initCompoment();//调用组件初始化方法

    }

    private void initCompoment() {
        et_userName = (EditTextDeleteView) findViewById(R.id.et_userName);
        et_password = (EditTextPwdDeleteView) findViewById(R.id.et_password);
    }

    public void login(View v) throws Exception{
        closeKeyboard();
        //1.获取输入的用户名密码
        final String userName = et_userName.getText().toString();
        final String password = et_password.getText().toString();
        if(userName==""||userName.length()==0||userName==null){
            Toast.makeText(LoginActivity.this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
        }else if(password==""||password.length()==0||password==null){
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            //start the progress dialog
            mProgressDialog = ProgressDialog.show(LoginActivity.this, "", "正在登陆...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        String url =  OkhttpHelper.getIp()+"/shopsystem/androidUser/login2";
                        OkhttpHelper.login(url, userName,password, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                        // dismiss the progress dialog
                                        mProgressDialog.dismiss();
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
                                        final int userId = user.getInt("userId");
                                        final String nickName = user.getString("nickName");
                                        final String school = user.getString("school");
                                        final String profilePhoto = user.getString("profilePhoto");
                                        if(user.getInt("state")==2){
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
                                                    Toast.makeText(LoginActivity.this, "欢迎"+nickName+"，登录淘校园！", Toast.LENGTH_SHORT).show();
                                                    //记住用户名、密码 将自动登录设为ture
                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putString("userName", userName);
                                                    editor.putString("userPwd",password);
                                                    editor.putInt("userId",userId);
                                                    editor.putString("nickName",nickName);
                                                    editor.putString("school",school);
                                                    editor.putString("profilePhoto",profilePhoto);
                                                    editor.putBoolean("is_auto",true);
                                                    editor.commit();
                                                    EMClient.getInstance().login(userName, password, new EMCallBack() {
                                                        @Override
                                                        public void onSuccess() {
                                                            //从本地DB加载到程序中
                                                            // 加载所有会话到内存
                                                            EMClient.getInstance().chatManager().loadAllConversations();
                                                            Log.e("myshop","登录成功");
                                                        }

                                                        @Override
                                                        public void onError(int i, String s) {
                                                            Log.e("myshop","登录失败"+i+","+s);
                                                        }

                                                        @Override
                                                        public void onProgress(int i, String s) {

                                                        }
                                                    });
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
                                // dismiss the progress dialog
                                mProgressDialog.dismiss();
                            }

                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    //只是关闭软键盘
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
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

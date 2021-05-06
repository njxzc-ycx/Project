package com.njxzc.myshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LauncherActivity extends AppCompatActivity {

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Model.getInstance().init(this);
        //第一个参数为存储的文件名，第二个参数为默认的操作模式
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //第一个参数为键的名称，第二个0参数为找不到值的时候的默认值
        if(sp.getBoolean("is_auto",false)){
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/findUserByUserName";
                    String userName = sp.getString("userName","");
                    OkhttpHelper.findUserByUserName(url, userName, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //在主线程中执行
                                            Toast.makeText(LauncherActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            startMainActivity();
                                        }
                                    }, 2000);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();

                            try {
                                JSONObject user = new JSONObject(responseData);
                                if(user.getInt("state")==2){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LauncherActivity.this,"您的账号已被封禁！如有疑问,请联系客服",Toast.LENGTH_SHORT).show();
                                            new Thread(){
                                                @Override
                                                public void run() {
                                                    super.run();
                                                    EMClient.getInstance().logout(true);
                                                }
                                            }.start();
                                            //将sharedPreferences中保存的用户名，密码清空 自动登录设为false
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putString("userName","");
                                            editor.putString("userPwd","");
                                            editor.putString("profilePhoto","");
                                            editor.putString("school","");
                                            editor.putString("nickName","");
                                            editor.putInt("userId",0);
                                            editor.putBoolean("is_auto",false);
                                            editor.commit();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //在主线程中执行
                                                    startLoginActivity();
                                                }
                                            }, 2000);
                                        }
                                    });
                                }else {
                                    String userName = sp.getString("userName","");
                                    String password = sp.getString("userPwd","");
                                    EMClient.getInstance().login(userName, password, new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            //从本地DB加载到程序中
                                            // 加载所有会话到内存
                                            EMClient.getInstance().chatManager().loadAllConversations();
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    startMainActivity();
                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            Log.e("myshop","登录失败"+i+","+s);
                                        }

                                        @Override
                                        public void onProgress(int i, String s) {

                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            });

        }
        else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //在主线程中执行
                    startLoginActivity();
                }
            }, 2000);
        }
    }

    /*
    * 启动登录页面
    */
    private void startLoginActivity() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*
    * 启动主页面
    */
    private void startMainActivity() {
        /*String nickName = sp.getString("nickName","");
        Toast.makeText(LauncherActivity.this,"欢迎"+nickName+"，登录淘校园！", Toast.LENGTH_SHORT).show();*/
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}

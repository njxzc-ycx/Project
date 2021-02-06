package com.njxzc.myshop.personal_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.njxzc.myshop.LoginActivity;
import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.EditTextPwdDeleteView;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdatePwdActivity extends AppCompatActivity {


    private EditTextPwdDeleteView et_oldPwd;
    private EditTextPwdDeleteView et_newPwd;
    AppInfo appInfo=new AppInfo();
    User user = new User();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_pwd);
        initCompoment();
    }

    private void initCompoment() {
        et_newPwd = (EditTextPwdDeleteView) findViewById(R.id.et_newPwd);
        et_oldPwd = (EditTextPwdDeleteView) findViewById(R.id.et_oldPwd);
    }

    public void submit(View v) throws Exception{
        appInfo = (AppInfo)getApplication();
        user=appInfo.getUser();
        final String userId = String.valueOf(user.getUserId());
        //1.获取输入的用户名密码
        final String oldPwd = et_oldPwd.getText().toString();
        final String newPwd = et_newPwd.getText().toString();
        if(oldPwd==""||oldPwd.length()==0||oldPwd==null){
            Toast.makeText(UpdatePwdActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(newPwd==""||newPwd.length()==0||newPwd==null){
            Toast.makeText(UpdatePwdActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String url = "http://192.168.0.103:8088/shopsystem/androidUser/updateUserPwd";
                        OkhttpHelper.updateUserPwd(url, oldPwd, newPwd, userId, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdatePwdActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseData = response.body().string();
                                if (responseData.equals("oldPwdError")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UpdatePwdActivity.this, "原密码错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else if(responseData.equals("updateUserPwdFail")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UpdatePwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UpdatePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                            UpdatePwdActivity.this.finish();
                                        }
                                    });
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


    public void backSettings(View v) throws Exception{
        this.finish();
    }
}

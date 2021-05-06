package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.njxzc.myshop.R;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.customview.EditTextPwdDeleteView;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdatePwdActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    private EditTextPwdDeleteView et_oldPwd;
    private EditTextPwdDeleteView et_newPwd;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_pwd);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initCompoment();
    }

    private void initCompoment() {
        et_newPwd = (EditTextPwdDeleteView) findViewById(R.id.et_newPwd);
        et_oldPwd = (EditTextPwdDeleteView) findViewById(R.id.et_oldPwd);
    }

    public void submit(View v) throws Exception{
        closeKeyboard();
        final String userId = String.valueOf(sp.getInt("userId",0));
        final String userName = sp.getString("userName","");
        //1.获取输入的用户名密码
        final String oldPwd = et_oldPwd.getText().toString();
        final String newPwd = et_newPwd.getText().toString();
        if(oldPwd==""||oldPwd.length()==0||oldPwd==null){
            Toast.makeText(UpdatePwdActivity.this, "原密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(newPwd==""||newPwd.length()==0||newPwd==null){
            Toast.makeText(UpdatePwdActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!isPassword(newPwd)){
            Toast.makeText(getApplicationContext(), "请输入正确格式的密码", Toast.LENGTH_SHORT).show();
        }else if(newPwd.equals(oldPwd)){
            Toast.makeText(UpdatePwdActivity.this, "新密码不能与原密码相同", Toast.LENGTH_SHORT).show();
        }else {
            //start the progress dialog
            mProgressDialog = ProgressDialog.show(UpdatePwdActivity.this, "", "正在提交...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/updateUserPwd";
                        OkhttpHelper.updateUserPwd(url, oldPwd, newPwd, userId, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdatePwdActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                        // dismiss the progress dialog
                                        mProgressDialog.dismiss();
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
                                    String url = "http://a1.easemob.com/1178210225148941/myshop/users/"+userName+"/password";
                                    OkhttpHelper.updateIMPWD(url, newPwd, new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            try {
                                                String responseData = response.body().string();
                                                System.out.println(responseData);
                                                JSONObject result = new JSONObject(responseData);
                                                String action = result.getString("action");
                                                if (action.equals("set user password")){
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(UpdatePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                            sp.edit().putString("userPwd",newPwd).commit();
                                                            UpdatePwdActivity.this.finish();
                                                        }
                                                    });
                                                }else {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(UpdatePwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
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

    public boolean isPassword(String password){
        String regex="^(?![0-9])(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(password);
        boolean isMatch=m.matches();
        return isMatch;
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

    public void backSettings(View v) throws Exception{
        this.finish();
    }
}

package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.njxzc.myshop.LoginActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class UpdateNickNameActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private EditText et_nikeName;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nick_name);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        init();
    }

    private void initView() {
        et_nikeName = (EditText) findViewById(R.id.et_nikeName);
    }

    private void init() {
        Intent intent = this.getIntent();
        String nickName = intent.getExtras().getString("nickName");
        et_nikeName.setText(nickName);
    }

    public void submit(View v) throws Exception{
        closeKeyboard();
        final int userId = sp.getInt("userId",0);
        final String nickName = et_nikeName.getText().toString();
        if(nickName==""||nickName.length()==0||nickName==null){
            Toast.makeText(UpdateNickNameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
        }else{
            //start the progress dialog
            mProgressDialog = ProgressDialog.show(UpdateNickNameActivity.this, "", "正在提交...");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/updateNickName";
                    OkhttpHelper.updateNickName(url,nickName,String.valueOf(userId),new okhttp3.Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UpdateNickNameActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    // dismiss the progress dialog
                                    mProgressDialog.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseData = response.body().string();
                            System.out.println(responseData);
                            if (responseData.equals("nickNameExist")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateNickNameActivity.this, "昵称已经被占用", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if(responseData.equals("updateNickNameFail")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateNickNameActivity.this, "昵称修改失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else if(responseData.equals("updateNickNameSuccess")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpdateNickNameActivity.this, "昵称修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                        intent.putExtra("flag","refreshNickName");
                                        intent.putExtra("nickName",nickName);
                                        LocalBroadcastManager.getInstance(UpdateNickNameActivity.this).sendBroadcast(intent);
                                        sendBroadcast(intent);
                                        sp.edit().putString("nickName",nickName).commit();
                                        UpdateNickNameActivity.this.finish();
                                    }
                                });
                            }
                            // dismiss the progress dialog
                            mProgressDialog.dismiss();
                        }
                    });

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

    public void backPersonalInfo(View v) throws Exception{
        this.finish();
    }
}

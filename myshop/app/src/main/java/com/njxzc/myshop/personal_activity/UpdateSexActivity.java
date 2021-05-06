package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.njxzc.myshop.R;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class UpdateSexActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private RadioButton rb_sex_man;
    private RadioButton rb_sex_woman;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sex);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        init();
        click();
    }


    private void initView() {
        rb_sex_man = (RadioButton) findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton) findViewById(R.id.rb_sex_woman);
    }


    private void init() {
        Intent intent = this.getIntent();
        int sex = intent.getExtras().getInt("sex");
        System.out.println(sex);
        if (sex==1){
            rb_sex_man.setChecked(true);
        }else if(sex==0){
            rb_sex_woman.setChecked(true);
        }
    }

    private void click() {
        if (!rb_sex_man.isChecked()) {
            rb_sex_man.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //start the progress dialog
                    mProgressDialog = ProgressDialog.show(UpdateSexActivity.this, "", "正在提交...");
                    //Toast.makeText(UpdateSexActivity.this,"男",Toast.LENGTH_SHORT).show();
                    final int userId = sp.getInt("userId",0);
                    final int sex = 1;
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/updateSex";
                                OkhttpHelper.updateSex(url,String.valueOf(sex),String.valueOf(userId),new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(UpdateSexActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                // dismiss the progress dialog
                                                mProgressDialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        System.out.println(responseData);
                                        if(responseData.equals("updateSexFail")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(UpdateSexActivity.this, "性别修改失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else if(responseData.equals("updateSexSuccess")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(UpdateSexActivity.this, "性别修改成功", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                    intent.putExtra("flag","refreshSex");
                                                    intent.putExtra("sex",sex);
                                                    LocalBroadcastManager.getInstance(UpdateSexActivity.this).sendBroadcast(intent);
                                                    sendBroadcast(intent);
                                                    UpdateSexActivity.this.finish();
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

            });
        }

        if (!rb_sex_woman.isChecked()) {
            rb_sex_woman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //start the progress dialog
                    mProgressDialog = ProgressDialog.show(UpdateSexActivity.this, "", "正在提交...");
                  //Toast.makeText(UpdateSexActivity.this,"女",Toast.LENGTH_SHORT).show();
                    final int userId = sp.getInt("userId",0);
                    final int sex = 0;
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/updateSex";
                            OkhttpHelper.updateSex(url,String.valueOf(sex),String.valueOf(userId),new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(UpdateSexActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String responseData = response.body().string();
                                    System.out.println(responseData);
                                    if(responseData.equals("updateSexFail")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(UpdateSexActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else if(responseData.equals("updateSexSuccess")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(UpdateSexActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                                                intent.putExtra("flag","refreshSex");
                                                intent.putExtra("sex",sex);
                                                LocalBroadcastManager.getInstance(UpdateSexActivity.this).sendBroadcast(intent);
                                                sendBroadcast(intent);
                                                UpdateSexActivity.this.finish();
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
            });
        }
    }
    public void backPersonalInfo(View v) throws Exception{
        this.finish();
    }
}

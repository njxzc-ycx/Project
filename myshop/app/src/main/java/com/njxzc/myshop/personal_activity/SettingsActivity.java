package com.njxzc.myshop.personal_activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.njxzc.myshop.LoginActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.adapter.SettingsAdapter;

/*
* 我的->设置
*/
public class SettingsActivity extends AppCompatActivity {
    private ListView lv_settings;
    private String[] settext;
    private String[] setvalue;
    private Button btn_logout;
    private SettingsAdapter adapter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initCompoment();//调用组件初始化方法
        init();

    }

    private void initCompoment() {
        lv_settings = (ListView) findViewById(R.id.lv_settings);
        btn_logout = (Button) findViewById(R.id.btn_logout);
    }


    private void init() {
        String userName = sp.getString("userName","");
        settext = new String[]{"修改手机号","修改密码"};
        setvalue = new String[]{userName,""};
        adapter = new SettingsAdapter(SettingsActivity.this,settext,setvalue);
        lv_settings.setAdapter(adapter);
        lv_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = settext[position];
                if(data.equals("修改手机号")){
                    Intent intent = new Intent(SettingsActivity.this,UpdatePhoneActivity.class);
                    startActivity(intent);
                    SettingsActivity.this.finish();
                }else if(data.equals("修改密码")){
                    Intent intent = new Intent(SettingsActivity.this,UpdatePwdActivity.class);
                    startActivity(intent);
                    //SettingsActivity.this.finish();
                }
            }
        });


        //退出按钮监听
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("您确定要退出当前账号？");
                //取消按钮
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                //确定按钮
                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
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
                        Intent intent = new Intent(SettingsActivity.this,LoginActivity.class);
                        //跳转到登录界面后，并将栈底的Activity全部都销毁
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        SettingsActivity.this.finish();
                    }
                });
                //显示对话框
                builder.create().show();

            }
        });
    }



    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }


}

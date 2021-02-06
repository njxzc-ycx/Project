package com.njxzc.myshop.personal_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.njxzc.myshop.R;
import com.njxzc.myshop.utils.HelpCenterAdapter;

/*
*我的->帮助中心
*/
public class HelpCenterActivity extends AppCompatActivity {

    private ListView lv_helpcenter;
    private String[] helps;
    private HelpCenterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        initCompoment();//调用组件初始化方法
        init();
    }

    private void initCompoment() {
        lv_helpcenter = (ListView) findViewById(R.id.lv_helpcenter);
    }

    private void init() {
        helps = new String[]{"APP使用指南","如何注册新账号","隐私政策","用户协议"};
        adapter = new HelpCenterAdapter(HelpCenterActivity.this,helps);
        lv_helpcenter.setAdapter(adapter);
        lv_helpcenter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = helps[position];
                if(data.equals("APP使用指南")){
                    Intent intent = new Intent(HelpCenterActivity.this,AppGuideActivity.class);
                    startActivity(intent);
                }else if(data.equals("如何注册新账号")){
                    Intent intent = new Intent(HelpCenterActivity.this,HowToRegisterActivity.class);
                    startActivity(intent);
                }else if(data.equals("隐私政策")){
                    Intent intent = new Intent(HelpCenterActivity.this,PrivacyPolicyActivity.class);
                    startActivity(intent);
                }else if(data.equals("用户协议")){
                    Intent intent = new Intent(HelpCenterActivity.this,UserAgressmentActivity.class);
                    startActivity(intent);
                }
            }
        });


    }


    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

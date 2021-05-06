package com.njxzc.myshop.newmsg_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.njxzc.myshop.R;

public class NewMsgActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_msg);
    }

    public void back(View v) throws Exception{
        this.finish();
    }
}

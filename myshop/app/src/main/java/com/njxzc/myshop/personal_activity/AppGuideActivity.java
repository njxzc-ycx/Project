package com.njxzc.myshop.personal_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.njxzc.myshop.R;

public class AppGuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_guide);
    }

    public void backHelpCenter(View v) throws Exception{
        this.finish();
    }
}

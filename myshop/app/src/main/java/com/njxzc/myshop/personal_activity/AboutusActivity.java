package com.njxzc.myshop.personal_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.njxzc.myshop.R;

/*
*我的->关于我们
*/
public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
    }

    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

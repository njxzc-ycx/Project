package com.njxzc.myshop.personal_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.njxzc.myshop.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        TextView tv_privacypolicy = (TextView) findViewById(R.id.tv_privacypolicy);
        tv_privacypolicy.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void backHelpCenter(View v) throws Exception{
        this.finish();
    }
}

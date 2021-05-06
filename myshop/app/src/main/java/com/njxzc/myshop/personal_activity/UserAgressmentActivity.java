package com.njxzc.myshop.personal_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.njxzc.myshop.R;

public class UserAgressmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agressment);
        TextView tv_useraggressment = (TextView) findViewById(R.id.tv_useraggressment);
        tv_useraggressment.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void backHelpCenter(View v) throws Exception{
        this.finish();
    }
}

package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.njxzc.myshop.personal_activity.MyOrdersActivity;

public class PaySuccessActivity extends AppCompatActivity {

    private Button btn_backMain;
    private Button btn_toMyOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        initView();
    }

    private void initView() {
        btn_backMain = (Button) findViewById(R.id.btn_backMain);
        btn_toMyOrders = (Button) findViewById(R.id.btn_toMyOrders);

        btn_backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaySuccessActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                PaySuccessActivity.this.finish();
            }
        });

        btn_toMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaySuccessActivity.this.finish();
            }
        });
    }

    public void back(View v) throws Exception{
        this.finish();
    }
}

package com.njxzc.myshop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.njxzc.myshop.adapter.InteractAdapter;
import com.njxzc.myshop.alipay.AuthResult;
import com.njxzc.myshop.alipay.OrderInfoUtil2_0;
import com.njxzc.myshop.alipay.PayResult;
import com.njxzc.myshop.personal_activity.MyOrdersActivity;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity {
    private TextView tv_totalPrice;
    private TextView tv_comName;
    private TextView tv_orderId;
    private RadioButton rb_alipay;
    private Button btn_toPay;
    private double total;
    private String comName;
    private int orderId;
    private int count;
    private int comId;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        super.onCreate(savedInstanceState);
        Model.getInstance().init(this);
        setContentView(R.layout.activity_pay);
        initView();
        init();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                int status = 1;
                                String url= OkhttpHelper.getIp()+"/shopsystem/androidOrder/updateCommodityInfoAndUserOrderInfoByComId";
                                OkhttpHelper.updateCommodityInfoAndUserOrderInfoByComId(url, String.valueOf(comId), String.valueOf(count),String.valueOf(orderId),String.valueOf(status), new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(PayActivity.this,PayFailActivity.class);
                                                startActivity(intent);
                                                PayActivity.this.finish();
                                                Toast.makeText(PayActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                        if(responseData.equals("success")){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent1 = new Intent("android.intent.action.CART_BROADCAST");
                                                    intent1.putExtra("flag", "refreshOrder");
                                                    LocalBroadcastManager.getInstance(PayActivity.this).sendBroadcast(intent1);
                                                    PayActivity.this.sendBroadcast(intent1);
                                                    Intent intent = new Intent(PayActivity.this,PaySuccessActivity.class);
                                                    startActivity(intent);
                                                    PayActivity.this.finish();
                                                    Toast.makeText(PayActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }else {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent = new Intent(PayActivity.this,PayFailActivity.class);
                                                    startActivity(intent);
                                                    PayActivity.this.finish();
                                                    Toast.makeText(PayActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }


                                    }
                                });
                            }
                        });

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Intent intent = new Intent(PayActivity.this,PayFailActivity.class);
                        startActivity(intent);
                        PayActivity.this.finish();
                        Toast.makeText(PayActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,"授权失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    private void initView() {
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
        tv_orderId = (TextView) findViewById(R.id.tv_orderId);
        tv_comName = (TextView) findViewById(R.id.tv_comName);
        rb_alipay = (RadioButton) findViewById(R.id.rb_alipay);
        btn_toPay = (Button) findViewById(R.id.btn_toPay);


    }

    private void init() {
        Intent intent  = getIntent();
        total = intent.getDoubleExtra("total",0);
        orderId = intent.getIntExtra("orderId",0);
        comId = intent.getIntExtra("comId",0);
        count = intent.getIntExtra("count",0);
        comName = intent.getStringExtra("comName");
        tv_totalPrice.setText("应付款：￥"+String.valueOf(total));
        tv_comName.setText("商品名："+comName);
        tv_orderId.setText("订单编号："+orderId);
    }

    public void toPay(View v) throws Exception{
        if (rb_alipay.isChecked()){
            btn_toPay.setClickable(false);
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    int orderNo = orderId;
                    double amount = total;
                    String body = comName;
                    String url= OkhttpHelper.getIp()+"/shopsystem/alipay/createOrder";
                    OkhttpHelper.alipay(url, String.valueOf(orderNo), String.valueOf(amount), body,String.valueOf(comId),String.valueOf(count), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            System.out.println(responseData);

                                try {
                                    JSONObject datas = new JSONObject(responseData);
                                    final String orderInfo = (String) datas.get("data");
                                    System.out.println(orderInfo);
                                    if (orderInfo.equals("error1")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                                                builder.setTitle("提示");
                                                builder.setMessage("来晚一步，此商品已被他人买走或被下架");
                                                builder.setCancelable(false);
                                                //确定按钮
                                                builder.setPositiveButton("返回我的订单", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        Intent intent = new Intent(PayActivity.this,MyOrdersActivity.class);
                                                        startActivity(intent);
                                                        PayActivity.this.finish();
                                                    }
                                                });
                                                //显示对话框
                                                builder.create().show();
                                            }
                                        });
                                    }else if (orderInfo.equals("error2")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(PayActivity.this);
                                                builder.setTitle("提示");
                                                builder.setMessage("来晚一步，此商品数量不足，请重新下单");
                                                builder.setCancelable(false);
                                                //确定按钮
                                                builder.setPositiveButton("返回我的订单", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int which) {
                                                        Intent intent = new Intent(PayActivity.this,MyOrdersActivity.class);
                                                        startActivity(intent);
                                                        PayActivity.this.finish();
                                                    }
                                                });
                                                //显示对话框
                                                builder.create().show();
                                            }
                                        });
                                    }else {
                                        final Runnable payRunnable = new Runnable() {

                                            @Override
                                            public void run() {
                                                PayTask alipay = new PayTask(PayActivity.this);
                                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                                Log.i("msp", result.toString());

                                                Message msg = new Message();
                                                msg.what = SDK_PAY_FLAG;
                                                msg.obj = result;
                                                mHandler.sendMessage(msg);
                                            }
                                        };

                                        // 必须异步调用
                                        Thread payThread = new Thread(payRunnable);
                                        payThread.start();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }



                    });
                }
            });
        }else {
            Toast.makeText(PayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
        }

    }




    public void back(View v) throws Exception{
        this.finish();
    }
}

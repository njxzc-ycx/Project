package com.njxzc.myshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.njxzc.myshop.adapter.AllAddressAdapter;
import com.njxzc.myshop.personal_activity.AddressesActivity;
import com.njxzc.myshop.personal_activity.MyOrdersActivity;
import com.njxzc.myshop.personal_activity.SettingsActivity;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BuyActivity extends AppCompatActivity {

    private ArrayList<Map<String,Object>> comDatas;
    private ArrayList<Map<String,Object>> addressDatas;
    private TextView tv_comName;
    private TextView tv_currentPrice;
    private TextView tv_counts;
    private TextView tv_addressDetail;
    private TextView tv_area;
    private TextView tv_linkman;
    private TextView tv_linkphone;
    private TextView tv_totalPrice;
    private Button btn_submitOrder;
    private ImageView iv_comImageMain;
    private ImageView iv_reduce;
    private ImageView iv_add;
    private SwipeRefreshLayout sr_buy;
    private LinearLayout ll_toAddress;

    private int comId;
    private int counts;
    private int addressId=0;
    private int userId;
    private String comName;
    private Double currentPrice;
    private String updateTime;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        initCom();
        initAddress();
        refresh();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshAddress".equals(msg)){
                    String area = intent.getStringExtra("area");
                    String addressDetail = intent.getStringExtra("addressDetail");
                    String linkPhone = intent.getStringExtra("linkPhone");
                    String linkman = intent.getStringExtra("linkman");
                    tv_addressDetail.setText(addressDetail);
                    tv_area.setText(area);
                    tv_linkman.setText(linkman);
                    tv_linkphone.setText(linkPhone);
                    addressId = intent.getIntExtra("addressId",0);

                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);

        submitOrder();

    }



    private void initView() {
        tv_comName = (TextView) findViewById(R.id.tv_comName);
        tv_currentPrice = (TextView) findViewById(R.id.tv_currentPrice);
        tv_counts = (TextView) findViewById(R.id.tv_counts);
        tv_linkman = (TextView) findViewById(R.id.tv_linkman);
        tv_addressDetail = (TextView) findViewById(R.id.tv_addressDetail);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_linkphone = (TextView) findViewById(R.id.tv_linkphone);
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
        btn_submitOrder = (Button) findViewById(R.id.btn_submitOrder);
        iv_comImageMain = (ImageView) findViewById(R.id.iv_comImageMain);
        iv_comImageMain.setScaleType(ImageView.ScaleType.FIT_XY);
        iv_reduce = (ImageView) findViewById(R.id.iv_reduce);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        ll_toAddress = (LinearLayout) findViewById(R.id.ll_toAddress);
        sr_buy = (SwipeRefreshLayout) findViewById(R.id.sr_buy);
        ll_toAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyActivity.this,ChooseAddressActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCom() {
        Intent intent = getIntent();
        comDatas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
        System.out.println(comDatas);
        int position = intent.getIntExtra("position", 0);//获取第一个页面传递的position
        comId = Integer.parseInt(comDatas.get(position).get("comId").toString());
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url =  OkhttpHelper.getIp()+"/shopsystem/androidCom/findComByComId";
                OkhttpHelper.findComByComId(url, String.valueOf(comId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BuyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            final JSONObject com = new JSONObject(responseData);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        int sellerId = com.getInt("sellerId");
                                        counts = com.getInt("count");
                                        currentPrice = com.getDouble("currentPrice");
                                        comName = com.getString("comName");
                                        String comImageMain = com.getString("comImageMain");
                                        updateTime = com.getString("updateTime");
                                        Glide.with(BuyActivity.this)
                                                .load(comImageMain).asBitmap()
                                                .error(R.drawable.errorpic)
                                                .placeholder(R.drawable.defaultpic)
                                                .into(iv_comImageMain);
                                        tv_comName.setText(comName);
                                        tv_currentPrice.setText("￥"+String.valueOf(currentPrice));
                                        tv_totalPrice.setText("总金额：￥"+String.valueOf(currentPrice));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_counts.getText().toString());
                if (count>=counts){
                    Toast.makeText(BuyActivity.this,"此商品数量只有"+counts+"件",Toast.LENGTH_SHORT).show();
                }else {
                    count++;
                    Double total = currentPrice*count;
                    tv_counts.setText(String.valueOf(count));
                    tv_totalPrice.setText("总金额：￥"+String.valueOf(total));
                }
            }
        });

        iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(tv_counts.getText().toString());
                if(count>1){
                    count--;
                    Double total = currentPrice*count;
                    tv_counts.setText(String.valueOf(count));
                    tv_totalPrice.setText("总金额：￥"+String.valueOf(total));
                }
            }
        });


    }


    private void initAddress() {
        userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url =  OkhttpHelper.getIp()+"/shopsystem/androidAddress/findDefaultAddressByUserId";
                OkhttpHelper.findAllAddressByUserId(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BuyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            JSONObject address = new JSONObject(responseData);
                            addressId = address.getInt("addressId");
                            final String addressDetail = address.getString("address");
                            final String area = address.getString("area");
                            final String linkman = address.getString("linkman");
                            final String linkphone = address.getString("phone");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_addressDetail.setText(addressDetail);
                                    tv_area.setText(area);
                                    tv_linkman.setText(linkman);
                                    tv_linkphone.setText(linkphone);
                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }


    private void submitOrder() {
        btn_submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addressId==0){
                    Toast.makeText(BuyActivity.this,"请选择收货地址",Toast.LENGTH_SHORT).show();
                }else {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidCom/findComByComId";
                            OkhttpHelper.findComByComId(url, String.valueOf(comId), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(BuyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    try {
                                        final String responseData = response.body().string();
                                        JSONObject com = new JSONObject(responseData);
                                        String nowUpdateTime = com.getString("updateTime");
                                        int flag = com.getInt("flag");
                                        if (flag==2||flag==3){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(BuyActivity.this,"来晚一步,此商品已被下架或被买走T T",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            if (nowUpdateTime.equals(updateTime)){
                                                final int count = Integer.parseInt(tv_counts.getText().toString());
                                                final Double total = count * currentPrice;
                                                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                                                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                                final String orderTime = formatter.format(curDate);
                                                String url =  OkhttpHelper.getIp()+"/shopsystem/androidOrder/buyCom";
                                                OkhttpHelper.buyCom(url, String.valueOf(comId), String.valueOf(userId), String.valueOf(addressId), orderTime,
                                                        String.valueOf(total), String.valueOf(count), new Callback() {
                                                            @Override
                                                            public void onFailure(Call call, IOException e) {
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(BuyActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onResponse(Call call, Response response) throws IOException {
                                                                final int responseData = Integer.parseInt(response.body().string());
                                                                final int orderId = responseData;
                                                                if (responseData!=0&&responseData!=1){
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Intent intent = new Intent(BuyActivity.this,PayActivity.class);
                                                                            intent.putExtra("total",total);
                                                                            intent.putExtra("orderId",orderId);
                                                                            intent.putExtra("comName",comName);
                                                                            intent.putExtra("comId",comId);
                                                                            intent.putExtra("count",count);
                                                                            startActivity(intent);
                                                                            BuyActivity.this.finish();
                                                                        }
                                                                    });
                                                                }else if (responseData==1){
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            //Toast.makeText(BuyActivity.this, "    您有此商品未支付的订单，\n前往“我的订单”支付或取消订单", Toast.LENGTH_SHORT).show();
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(BuyActivity.this);
                                                                            builder.setTitle("提示");
                                                                            builder.setMessage("您有此商品未支付的订单");
                                                                            //取消按钮
                                                                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                                }
                                                                            });
                                                                            //确定按钮
                                                                            builder.setPositiveButton("前往支付", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                                                    Intent intent = new Intent(BuyActivity.this,MyOrdersActivity.class);
                                                                                    startActivity(intent);
                                                                                    BuyActivity.this.finish();
                                                                                }
                                                                            });
                                                                            //显示对话框
                                                                            builder.create().show();
                                                                        }

                                                                    });


                                                                }
                                                                else {
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            Toast.makeText(BuyActivity.this, "提交订单失败", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });

                                            }else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(BuyActivity.this,"商品信息已被卖家更改,如需购买,请下拉刷新此页面",Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    });

                }


            }
        });
    }

    private void refresh() {
        sr_buy.setColorSchemeColors(Color.BLUE,Color.GREEN);
        sr_buy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initCom();
                        counts=1;
                        tv_counts.setText(String.valueOf(counts));
                        Toast.makeText(BuyActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        sr_buy.setRefreshing(false);
                    }
                },2000);
            }
        });

    }

    public void back(View v) throws Exception{
        this.finish();
    }


}

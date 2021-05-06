package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OrderDetailActivity extends AppCompatActivity {
    private ArrayList<Map<String,Object>> datas;
    private ArrayList<Map<String,Object>> comdatas;
    private TextView tv_status;
    private TextView tv_orderId;
    private TextView tv_orderTime;
    private TextView tv_area;
    private TextView tv_addressDetail;
    private TextView tv_linkman;
    private TextView tv_linkphone;
    private ImageView iv_comImageMain;
    private TextView tv_comName;
    private TextView tv_currentPrice;
    private TextView tv_totalPrice;
    private LinearLayout ll_comDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Model.getInstance().init(this);
        initView();
        init();
    }

    private void initView() {
        tv_status = (TextView) findViewById(R.id.tv_status);
        ll_comDetail = (LinearLayout) findViewById(R.id.ll_comDetail);
        tv_orderId = (TextView) findViewById(R.id.tv_orderId);
        tv_orderTime = (TextView) findViewById(R.id.tv_orderTime);
        tv_area = (TextView) findViewById(R.id.tv_area);
        tv_addressDetail = (TextView) findViewById(R.id.tv_addressDetail);
        tv_linkman = (TextView) findViewById(R.id.tv_linkman);
        tv_linkphone = (TextView) findViewById(R.id.tv_linkphone);
        tv_comName = (TextView) findViewById(R.id.tv_comName);
        tv_currentPrice = (TextView) findViewById(R.id.tv_currentPrice);
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
        iv_comImageMain = (ImageView) findViewById(R.id.iv_comImageMain);
        iv_comImageMain.setScaleType(ImageView.ScaleType.FIT_XY);

        ll_comDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailActivity.this,ComDetailActivity.class);
                intent.putExtra("position",0);
                intent.putExtra("datas",(Serializable) comdatas);
                startActivity(intent);
            }
        });
    }

    private void init() {
        Intent intent = getIntent();
        datas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
        int position = intent.getIntExtra("position", 0);
        int status = (int) datas.get(position).get("status");
        int orderId = (int) datas.get(position).get("orderId");
        int buyCount = (int) datas.get(position).get("buyCount");
        final int comId = (int) datas.get(position).get("comId");
        String orderTime = (String) datas.get(position).get("orderTime");
        String area = (String) datas.get(position).get("area");
        String addressDetail = (String) datas.get(position).get("addressDetail");
        String linkMan = (String) datas.get(position).get("linkMan");
        String linkPhone = (String) datas.get(position).get("linkPhone");
        String comName = (String) datas.get(position).get("comName");
        String comImageMain = (String) datas.get(position).get("comImageMain");
        Double currentPrice = (Double) datas.get(position).get("currentPrice");
        Double totalPrice = (Double) datas.get(position).get("total");
        if (status==0){
            tv_status.setText("待买家付款");
        }else if(status==1){
            tv_status.setText("买家已付款，请尽快发货");
        }else if(status==2){
            tv_status.setText("卖家已发货，等待买家收货");
        }else if(status==3){
            tv_status.setText("买家确认收货，交易完成");
        }else if(status==4){
            tv_status.setText("卖家关闭了订单");
        }else if(status==5){
            tv_status.setText("买家关闭了订单");
        }
        tv_comName.setText(comName);
        tv_orderTime.setText(orderTime);
        tv_area.setText(area);
        tv_addressDetail.setText(addressDetail);
        tv_linkman.setText(linkMan);
        tv_linkphone.setText(linkPhone);
        tv_orderId.setText("订单号："+String.valueOf(orderId));
        tv_totalPrice.setText("共"+String.valueOf(buyCount)+"件商品，应付款："+String.valueOf(totalPrice)+"元");
        tv_currentPrice.setText("￥"+String.valueOf(currentPrice));

        Glide.with(this)
                .load(comImageMain).asBitmap()
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_comImageMain);

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
                                Toast.makeText(OrderDetailActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            final JSONObject com = new JSONObject(responseData);
                            JSONObject user = com.getJSONObject("user");
                            String school = user.getString("school");
                            String nickName = user.getString("nickName");
                            String profilePhoto = user.getString("profilePhoto");
                            String userName = user.getString("userName");
                            int sex = user.getInt("sex");
                            String comImageMain = com.getString("comImageMain");
                            String comImageOthers = com.getString("comImageOther");
                            String onTime = com.getString("onTime");
                            String inTime = com.getString("inTime");
                            String comName = com.getString("comName");
                            String updateTime = com.getString("updateTime");
                            int sellerId = com.getInt("sellerId");
                            int comId = com.getInt("comId");
                            int counts = com.getInt("count");
                            int hits = com.getInt("hits");
                            int flag = com.getInt("flag");
                            String des = com.getString("des");
                            Double currentPrice = com.getDouble("currentPrice");
                            Double primePrice = com.getDouble("primePrice");
                            int isBargain = com.getInt("isBargain");
                            int collects = com.getInt("collects");
                            Map map = new HashMap();
                            map.put("comImageMain",comImageMain);
                            map.put("comName",comName);
                            map.put("userName",userName);
                            map.put("des",des);
                            map.put("hits",hits);
                            map.put("counts",counts);
                            map.put("flag",flag);
                            map.put("comId",comId);
                            map.put("sellerId",sellerId);
                            map.put("currentPrice",currentPrice);
                            map.put("updateTime",updateTime);
                            map.put("primePrice",primePrice);
                            map.put("isBargain",isBargain);
                            map.put("collects",collects);
                            map.put("school",school);
                            map.put("nickName",nickName);
                            map.put("sex",sex);
                            map.put("profilePhoto",profilePhoto);
                            map.put("comImageOthers",comImageOthers);
                            map.put("onTime",onTime);
                            map.put("inTime",inTime);
                            comdatas = new ArrayList<Map<String, Object>>();
                            comdatas.add(map);
                            System.out.println(comdatas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

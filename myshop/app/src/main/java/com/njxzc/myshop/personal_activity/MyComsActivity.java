package com.njxzc.myshop.personal_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.BuyActivity;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.CollectsAdapter;
import com.njxzc.myshop.adapter.MyComsAdapter;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyComsActivity extends AppCompatActivity {

    private MyComsAdapter myComsAdapter;
    private ArrayList<Map<String,Object>> datas;
    private SwipeRefreshLayout sr_myComs;
    private ListView lv_myComs;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coms);
        Model.getInstance().init(this);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        init();
        initView();
        refresh();
        clickItem();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refresh".equals(msg)){
                    init();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        sr_myComs = (SwipeRefreshLayout) findViewById(R.id.sr_myComs);
        lv_myComs = (ListView) findViewById(R.id.lv_myComs);
    }

    private void init() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/findMyCommodityBySellerId";
                OkhttpHelper.getComs(url,String.valueOf(userId),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyComsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            System.out.println(responseData);
                            JSONArray jsonArray = new JSONArray(responseData);
                            datas = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject user = list.getJSONObject("user");
                                JSONObject typethird = list.getJSONObject("typethird");
                                String school = user.getString("school");
                                String thirdName = typethird.getString("thirdName");
                                String nickName = user.getString("nickName");
                                String profilePhoto = user.getString("profilePhoto");
                                String userName = user.getString("userName");
                                int sex = user.getInt("sex");
                                int thirdId = typethird.getInt("thirdId");
                                String comImageMain = list.getString("comImageMain");
                                String comImageOthers = list.getString("comImageOther");
                                String updateTime = list.getString("updateTime");
                                String onTime = list.getString("onTime");
                                String inTime = list.getString("inTime");
                                String comName = list.getString("comName");
                                int sellerId = list.getInt("sellerId");
                                int count = list.getInt("count");
                                int flag = list.getInt("flag");
                                int comId = list.getInt("comId");
                                int hits = list.getInt("hits");
                                String des = list.getString("des");
                                Double currentPrice = list.getDouble("currentPrice");
                                Double primePrice = list.getDouble("primePrice");
                                int isBargain = list.getInt("isBargain");
                                int collects = list.getInt("collects");
                                Map map = new HashMap();
                                map.put("comImageMain",comImageMain);
                                map.put("comName",comName);
                                map.put("userName",userName);
                                map.put("thirdId",thirdId);
                                map.put("thirdName",thirdName);
                                map.put("updateTime",updateTime);
                                map.put("count",count);
                                map.put("des",des);
                                map.put("hits",hits);
                                map.put("flag",flag);
                                map.put("comId",comId);
                                map.put("sellerId",sellerId);
                                map.put("currentPrice",currentPrice);
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
                                datas.add(map);
                            }
                            System.out.println(datas);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myComsAdapter = new MyComsAdapter(MyComsActivity.this,datas);
                                    lv_myComs.setAdapter(myComsAdapter);

                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyComsActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }


                });
            }
        });
    }

    private void clickItem() {
        lv_myComs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(MyCollectsActivity.this,
                        "点击位置"+position+datas.get(position-1).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MyComsActivity.this,ComDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
            }
        });
    }

    private void refresh() {
        sr_myComs.setColorSchemeColors(Color.BLUE,Color.GREEN);
        sr_myComs.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        Toast.makeText(MyComsActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                        sr_myComs.setRefreshing(false);
                    }
                },2000);
            }
        });

    }

    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

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

import com.njxzc.myshop.DiscussDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.MyDiscussAdapter;
import com.njxzc.myshop.adapter.PerDiscussAdapter;
import com.njxzc.myshop.utils.GetStandardDate;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyDiscussActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private ListView lv_myDis;
    private SwipeRefreshLayout sr_myDis;
    private ArrayList<Map<String,Object>> datas;
    private MyDiscussAdapter myDiscussAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discuss);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initView();
        initDatas();
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
                    initDatas();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }


    private void initView() {
        lv_myDis = (ListView) findViewById(R.id.lv_myDis);
        sr_myDis = (SwipeRefreshLayout) findViewById(R.id.sr_myDis);
    }


    private void initDatas() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/findMyDiscussByUserId";
                OkhttpHelper.getDiscussByUserId(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyDiscussActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String responseData = response.body().string();
                            JSONArray list = new JSONArray(responseData);
                            datas = new ArrayList<Map<String, Object>>();
                            for (int i=0;i<list.length();i++){
                                JSONObject data = list.getJSONObject(i);
                                JSONObject user = data.getJSONObject("user");
                                int discussId = data.getInt("discussId");
                                String discussDes = data.getString("discussDes");
                                String discussImages = data.getString("discussImages");
                                String discussTime = data.getString("discussTime");
                                //将字符串类型转为date类型
                                Date time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(discussTime);
                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                String tranDiscussTime = GetStandardDate.getTimeFormatText(time);
                                int discussUp = data.getInt("discussUp");
                                int discussComments = data.getInt("discussComments");
                                int discussHits = data.getInt("discussHits");
                                int userId = data.getInt("userId");
                                int isAnonymity = data.getInt("isAnonymity");
                                String nickName = user.getString("nickName");
                                String profilePhoto = user.getString("profilePhoto");
                                String school = user.getString("school");
                                int sex = user.getInt("sex");
                                Map map = new HashMap();
                                map.put("discussId",discussId);
                                map.put("discussDes",discussDes);
                                map.put("discussImages",discussImages);
                                map.put("discussTime",tranDiscussTime);
                                map.put("discussUp",discussUp);
                                map.put("isAnonymity",isAnonymity);
                                map.put("discussComments",discussComments);
                                map.put("discussHits",discussHits);
                                map.put("userId",userId);
                                map.put("nickName",nickName);
                                map.put("profilePhoto",profilePhoto);
                                map.put("school",school);
                                map.put("sex",sex);
                                datas.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myDiscussAdapter = new MyDiscussAdapter(MyDiscussActivity.this,datas);
                                    lv_myDis.setAdapter(myDiscussAdapter);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });

    }

    private void clickItem() {
        lv_myDis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int discussId = (int) datas.get(position).get("discussId");
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/addDisscussHits";
                            OkhttpHelper.addDisscussHits(url,String.valueOf(discussId),new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                }

                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Intent intent = new Intent(MyDiscussActivity.this,DiscussDetailActivity.class);
                intent.putExtra("discussId",discussId);
                startActivity(intent);
            }
        });


    }

    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }

    private void refresh() {
        sr_myDis.setColorSchemeColors(Color.BLUE);
        sr_myDis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        sr_myDis.setRefreshing(false);
                    }
                },2000);
            }
        });

    }
}

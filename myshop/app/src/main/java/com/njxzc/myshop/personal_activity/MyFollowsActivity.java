package com.njxzc.myshop.personal_activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.FollowsAdapter;
import com.njxzc.myshop.adapter.MySellsAdapter;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyFollowsActivity extends AppCompatActivity {
    private SwipeRefreshLayout sr_myFollows;
    private ListView lv_myFollows;
    private SharedPreferences sp;
    private FollowsAdapter followsAdapter;
    private ArrayList<Map<String,Object>> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follows);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initData();
        initView();
        refresh();
    }



    private void initView() {
        lv_myFollows = (ListView) findViewById(R.id.lv_myFollows);
        sr_myFollows = (SwipeRefreshLayout) findViewById(R.id.sr_myFollows);
    }

    private void initData() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String url= OkhttpHelper.getIp()+"/shopsystem/androidFollow/findFollowByUserId";
                OkhttpHelper.findFollowByUserId(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyFollowsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            JSONArray list = new JSONArray(responseData);
                            datas = new ArrayList<Map<String, Object>>();
                            for (int i = 0;i<list.length();i++){
                                JSONObject follows = list.getJSONObject(i);
                                int followId = follows.getInt("followId");
                                int followerId = follows.getInt("followerId");
                                int userId = follows.getInt("userId");
                                JSONObject user = follows.getJSONObject("user");
                                String nickName = user.getString("nickName");
                                String userName = user.getString("userName");
                                String school = user.getString("school");
                                String profilePhoto = user.getString("profilePhoto");
                                int sex = user.getInt("sex");
                                Map map = new HashMap();
                                map.put("followId",followId);
                                map.put("followerId",followerId);
                                map.put("userId",userId);
                                map.put("nickName",nickName);
                                map.put("userName",userName);
                                map.put("school",school);
                                map.put("profilePhoto",profilePhoto);
                                map.put("sex",sex);
                                datas.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    followsAdapter = new FollowsAdapter(MyFollowsActivity.this,datas);
                                    lv_myFollows.setAdapter(followsAdapter);
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

    private void refresh() {
        sr_myFollows.setColorSchemeColors(Color.BLUE);
        sr_myFollows.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        sr_myFollows.setRefreshing(false);
                    }
                },2000);
            }
        });
    }




    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

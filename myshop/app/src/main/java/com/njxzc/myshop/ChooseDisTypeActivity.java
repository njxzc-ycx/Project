package com.njxzc.myshop;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.DiscussTypeAdapter;
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

public class ChooseDisTypeActivity extends AppCompatActivity {

    private ListView lv_types;
    private ArrayList<Map<String,Object>> typesData;
    private DiscussTypeAdapter discussTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dis_type);
        Model.getInstance().init(this);
        init();
        initView();
        clickItem();
    }


    private void initView() {
        lv_types = (ListView) findViewById(R.id.lv_types);
    }

    private void init() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/findAllDiscussType";
                OkhttpHelper.getAllDiscussType(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChooseDisTypeActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            JSONArray jsonArray = new JSONArray(responseData);
                            typesData = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++) {
                                JSONObject typeFirst = jsonArray.getJSONObject(i);
                                int discussTypeId = typeFirst.getInt("discussTypeId");
                                String discussTypeName = typeFirst.getString("discussTypeName");
                                Map map = new HashMap();
                                map.put("discussTypeId",discussTypeId);
                                map.put("discussTypeName",discussTypeName);
                                typesData.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    discussTypeAdapter = new DiscussTypeAdapter(ChooseDisTypeActivity.this,typesData);
                                    lv_types.setAdapter(discussTypeAdapter);


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


    private void clickItem() {
        lv_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String discussTypeName = (String) typesData.get(position).get("discussTypeName");
                int discussTypeId = (int) typesData.get(position).get("discussTypeId");
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("flag","getDiscussType");
                intent.putExtra("discussTypeName",discussTypeName);
                intent.putExtra("discussTypeId",discussTypeId);
                LocalBroadcastManager.getInstance(ChooseDisTypeActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                ChooseDisTypeActivity.this.finish();
            }
        });

    }

    public void backHomepage(View v) throws Exception{
        this.finish();
    }
}

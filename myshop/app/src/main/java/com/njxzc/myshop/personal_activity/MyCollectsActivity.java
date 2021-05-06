package com.njxzc.myshop.personal_activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.CollectsAdapter;
import com.njxzc.myshop.adapter.ComAdapter;
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

public class MyCollectsActivity extends AppCompatActivity {

    private ArrayList<Map<String,Object>> datas;
    private CollectsAdapter collectsAdapter;
    private PullToRefreshListView mpullToRefresh_ListView;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collects);
        Model.getInstance().init(this);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        mpullToRefresh_ListView = (PullToRefreshListView) findViewById(R.id.lv_myCollects);

        // Set a listener to be invoked when the list should be refreshed.
        mpullToRefresh_ListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        // Add an end-of-list listener
        mpullToRefresh_ListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Toast.makeText(MyCollectsActivity.this, "没有啦，真的没有啦!", Toast.LENGTH_SHORT).show();
            }
        });


        init();
        clickItem();
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return datas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            final int userId = sp.getInt("userId",0);
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url= OkhttpHelper.getIp()+"/shopsystem/androidCollect/findCollectByUserId";
                    OkhttpHelper.getCollects(url,String.valueOf(userId), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyCollectsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                                    JSONObject com = list.getJSONObject("commodity");
                                    String school = user.getString("school");
                                    String nickName = user.getString("nickName");
                                    String profilePhoto = user.getString("profilePhoto");
                                    String userName = user.getString("userName");
                                    int sex = user.getInt("sex");
                                    String comImageMain = com.getString("comImageMain");
                                    String comImageOthers = com.getString("comImageOther");
                                    String updateTime = com.getString("updateTime");
                                    String onTime = com.getString("onTime");
                                    String inTime = com.getString("inTime");
                                    String comName = com.getString("comName");
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
                                    map.put("counts",counts);
                                    map.put("des",des);
                                    map.put("hits",hits);
                                    map.put("flag",flag);
                                    map.put("comId",comId);
                                    map.put("updateTime",updateTime);
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
                                        collectsAdapter = new CollectsAdapter(MyCollectsActivity.this,datas);
                                        mpullToRefresh_ListView.setAdapter(collectsAdapter);
                                        collectsAdapter.notifyDataSetChanged();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyCollectsActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }


                    });
                }
            });

            // Call onRefreshComplete when the list has been refreshed.
            mpullToRefresh_ListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void init() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidCollect/findCollectByUserId";
                OkhttpHelper.getCollects(url,String.valueOf(userId),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MyCollectsActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                                JSONObject com = list.getJSONObject("commodity");
                                String school = user.getString("school");
                                String nickName = user.getString("nickName");
                                String userName = user.getString("userName");
                                String profilePhoto = user.getString("profilePhoto");
                                int sex = user.getInt("sex");
                                String comImageMain = com.getString("comImageMain");
                                String comImageOthers = com.getString("comImageOther");
                                String updateTime = com.getString("updateTime");
                                String onTime = com.getString("onTime");
                                String inTime = com.getString("inTime");
                                String comName = com.getString("comName");
                                int sellerId = com.getInt("sellerId");
                                int comId = com.getInt("comId");
                                int hits = com.getInt("hits");
                                int flag = com.getInt("flag");
                                int counts = com.getInt("count");
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
                                map.put("comId",comId);
                                map.put("sellerId",sellerId);
                                map.put("counts",counts);
                                map.put("flag",flag);
                                map.put("updateTime",updateTime);
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
                                    collectsAdapter = new CollectsAdapter(MyCollectsActivity.this,datas);
                                    mpullToRefresh_ListView.setAdapter(collectsAdapter);
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MyCollectsActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }


                });
            }
        });

    }

    private void clickItem() {
        mpullToRefresh_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(MyCollectsActivity.this,
                        "点击位置"+position+datas.get(position-1).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MyCollectsActivity.this,ComDetailActivity.class);
                intent.putExtra("position",position-1);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
            }
        });
    }

    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }
}

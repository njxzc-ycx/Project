package com.njxzc.myshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.njxzc.myshop.adapter.AllComAdapter;
import com.njxzc.myshop.adapter.TypeComAdapter;
import com.njxzc.myshop.adapter.TypeThirdAdapter;
import com.njxzc.myshop.fragment.AllComFragment;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
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

public class TypeComActivity extends AppCompatActivity {
    private ArrayList<Map<String,Object>> datas;
    private ArrayList<Map<String,Object>> comDatas;
    private PullToRefreshGridView gv_listTypeCom;
    private TypeComAdapter typeComAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_com);
        Model.getInstance().init(this);


        initView();
        init();
        clickItem();

        // Set a listener to be invoked when the list should be refreshed.
        gv_listTypeCom.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向下拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向上拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask2().execute();
            }

        });

    }

    private void clickItem() {
        gv_listTypeCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(getActivity(),
                        "点击位置"+position+datas.get(position).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(TypeComActivity.this,ComDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) comDatas);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        gv_listTypeCom = (PullToRefreshGridView) findViewById(R.id.gv_listTypeCom);
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return comDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            Intent intent = getIntent();
            datas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
            int position = intent.getIntExtra("position", 0);//获取第一个页面传递的position
            final int thirdId = (int) datas.get(position).get("thirdId");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/findComByThirdId";
                    OkhttpHelper.getComByThirdId(url,String.valueOf(thirdId),new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TypeComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                System.out.println(responseData);
                                JSONArray jsonArray = new JSONArray(responseData);
                                comDatas = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject list = jsonArray.getJSONObject(i);
                                    JSONObject user = list.getJSONObject("user");
                                    String school = user.getString("school");
                                    String nickName = user.getString("nickName");
                                    String userName = user.getString("userName");
                                    String profilePhoto = user.getString("profilePhoto");
                                    int sex = user.getInt("sex");
                                    String comImageMain = list.getString("comImageMain");
                                    String comImageOthers = list.getString("comImageOther");
                                    String updateTime = list.getString("updateTime");
                                    String onTime = list.getString("onTime");
                                    String inTime = list.getString("inTime");
                                    String comName = list.getString("comName");
                                    int sellerId = list.getInt("sellerId");
                                    int flag = list.getInt("flag");
                                    int comId = list.getInt("comId");
                                    int counts = list.getInt("count");
                                    int hits = list.getInt("hits");
                                    String des = list.getString("des");
                                    Double currentPrice = list.getDouble("currentPrice");
                                    Double primePrice = list.getDouble("primePrice");
                                    int isBargain = list.getInt("isBargain");
                                    int collects = list.getInt("collects");
                                    Map map = new HashMap();
                                    map.put("comImageMain", comImageMain);
                                    map.put("comName", comName);
                                    map.put("userName", userName);
                                    map.put("updateTime", updateTime);
                                    map.put("des", des);
                                    map.put("hits", hits);
                                    map.put("comId", comId);
                                    map.put("flag", flag);
                                    map.put("counts", counts);
                                    map.put("sellerId", sellerId);
                                    map.put("currentPrice", currentPrice);
                                    map.put("primePrice", primePrice);
                                    map.put("isBargain", isBargain);
                                    map.put("collects", collects);
                                    map.put("school", school);
                                    map.put("nickName", nickName);
                                    map.put("sex", sex);
                                    map.put("profilePhoto", profilePhoto);
                                    map.put("comImageOthers", comImageOthers);
                                    map.put("onTime", onTime);
                                    map.put("inTime", inTime);
                                    comDatas.add(map);
                                }
                                System.out.println(comDatas);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        typeComAdapter = new TypeComAdapter(TypeComActivity.this, comDatas);
                                        gv_listTypeCom.setAdapter(typeComAdapter);
                                        typeComAdapter.notifyDataSetChanged();
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    });
                }
            });
            // Call onRefreshComplete when the list has been refreshed.
            gv_listTypeCom.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private class GetDataTask2 extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return comDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            if(typeComAdapter.count==comDatas.size()){
                Toast.makeText(TypeComActivity.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
            }
            typeComAdapter.count += 10;
            typeComAdapter.notifyDataSetChanged();


            // Call onRefreshComplete when the list has been refreshed.
            gv_listTypeCom.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void init() {
        Intent intent = getIntent();
        datas = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("datas");
        int position = intent.getIntExtra("position", 0);//获取第一个页面传递的position
        final int thirdId = (int) datas.get(position).get("thirdId");
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/findComByThirdId";
                OkhttpHelper.getComByThirdId(url,String.valueOf(thirdId),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TypeComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            typeComAdapter.count=10;
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            System.out.println(responseData);
                            JSONArray jsonArray = new JSONArray(responseData);
                            comDatas = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject user = list.getJSONObject("user");
                                String school = user.getString("school");
                                String nickName = user.getString("nickName");
                                String userName = user.getString("userName");
                                String profilePhoto = user.getString("profilePhoto");
                                int sex = user.getInt("sex");
                                String comImageMain = list.getString("comImageMain");
                                String comImageOthers = list.getString("comImageOther");
                                String updateTime = list.getString("updateTime");
                                String onTime = list.getString("onTime");
                                String inTime = list.getString("inTime");
                                String comName = list.getString("comName");
                                int sellerId = list.getInt("sellerId");
                                int counts = list.getInt("count");
                                int comId = list.getInt("comId");
                                int hits = list.getInt("hits");
                                int flag = list.getInt("flag");
                                String des = list.getString("des");
                                Double currentPrice = list.getDouble("currentPrice");
                                Double primePrice = list.getDouble("primePrice");
                                int isBargain = list.getInt("isBargain");
                                int collects = list.getInt("collects");
                                Map map = new HashMap();
                                map.put("comImageMain", comImageMain);
                                map.put("comName", comName);
                                map.put("userName", userName);
                                map.put("updateTime", updateTime);
                                map.put("des", des);
                                map.put("hits", hits);
                                map.put("counts", counts);
                                map.put("comId", comId);
                                map.put("flag", flag);
                                map.put("sellerId", sellerId);
                                map.put("currentPrice", currentPrice);
                                map.put("primePrice", primePrice);
                                map.put("isBargain", isBargain);
                                map.put("collects", collects);
                                map.put("school", school);
                                map.put("nickName", nickName);
                                map.put("sex", sex);
                                map.put("profilePhoto", profilePhoto);
                                map.put("comImageOthers", comImageOthers);
                                map.put("onTime", onTime);
                                map.put("inTime", inTime);
                                comDatas.add(map);
                            }
                            System.out.println(comDatas);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    typeComAdapter = new TypeComAdapter(TypeComActivity.this, comDatas);
                                    gv_listTypeCom.setAdapter(typeComAdapter);
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

    public void back(View v) throws Exception{
        this.finish();
    }

    public void toSearch(View v) throws Exception{
        Intent intent = new Intent(TypeComActivity.this,SearchActivity.class);
        startActivity(intent);
    }
}

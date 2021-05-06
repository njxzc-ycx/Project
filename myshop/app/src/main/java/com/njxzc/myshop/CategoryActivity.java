package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.AllComAdapter;
import com.njxzc.myshop.adapter.TypeFirstAdapter;
import com.njxzc.myshop.adapter.TypeSecondAdapter;
import com.njxzc.myshop.adapter.TypeThirdAdapter;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CategoryActivity extends AppCompatActivity {
    private ArrayList<Map<String,Object>> typeFirstDatas;
    private ArrayList<Map<String,Object>> typeSecondDatas;
    private ArrayList<Map<String,Object>> typeThirdDatas;
    private TypeFirstAdapter typeFirstAdapter;
    private TypeSecondAdapter typeSecondAdapter;
    private ListView lv_typeFirst;
    private ListView lv_typeSecond;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Model.getInstance().init(this);
        initView();
        init();
        lv_Click();
        /*gv_Click();*/
    }

    private void initView() {
        lv_typeFirst = (ListView) findViewById(R.id.lv_typeFirst);
        lv_typeSecond = (ListView) findViewById(R.id.lv_typeSecond);
    }

    private void init() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidType/findAllTypefirst";
                OkhttpHelper.getAllTypefirst(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CategoryActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            JSONArray jsonArray = new JSONArray(responseData);
                            typeFirstDatas = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++) {
                                JSONObject typeFirst = jsonArray.getJSONObject(i);
                                int firstId = typeFirst.getInt("firstId");
                                String firstName = typeFirst.getString("firstName");
                                Map map = new HashMap();
                                map.put("firstId",firstId);
                                map.put("firstName",firstName);
                                typeFirstDatas.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    typeFirstAdapter = new TypeFirstAdapter(CategoryActivity.this,typeFirstDatas);
                                    lv_typeFirst.setAdapter(typeFirstAdapter);
                                    typeFirstAdapter.setSelectedPosition(0);
                                    typeFirstAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                });
            }
        });

        final int firstId = 1;
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidType/findAllTypesecondByFirstId";
                OkhttpHelper.getAllTypesecond(url,String.valueOf(firstId),new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CategoryActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            JSONArray jsonArray = new JSONArray(responseData);
                            typeSecondDatas = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++) {
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject typesecond = list.getJSONObject("typesecond");
                                JSONArray typethirds = list.getJSONArray("typethird");
                                int secondId = typesecond.getInt("secondId");
                                String secondName = typesecond.getString("secondName");
                                Map map = new HashMap();
                                map.put("secondId",secondId);
                                map.put("secondName",secondName);
                                typeThirdDatas = new ArrayList<>();
                                for (int j=0;j<typethirds.length();j++){
                                    JSONObject typethird = typethirds.getJSONObject(j);
                                    int thirdId = typethird.getInt("thirdId");
                                    String thirdName = typethird.getString("thirdName");
                                    String thirdPicture = typethird.getString("thirdPicture");
                                    Map map1 = new HashMap();
                                    map1.put("thirdId",thirdId);
                                    map1.put("thirdName",thirdName);
                                    map1.put("thirdPicture",thirdPicture);
                                    typeThirdDatas.add(map1);
                                }
                                map.put("typeThirdDatas",typeThirdDatas);
                                typeSecondDatas.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    typeSecondAdapter=new TypeSecondAdapter(CategoryActivity.this,typeSecondDatas);
                                    lv_typeSecond.setAdapter(typeSecondAdapter);

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

    private void lv_Click() {
        lv_typeFirst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //选中item变白色
                typeFirstAdapter.setSelectedPosition(i);
                typeFirstAdapter.notifyDataSetChanged();
                final int firstId = (int) typeFirstDatas.get(i).get("firstId");
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        String url= OkhttpHelper.getIp()+"/shopsystem/androidType/findAllTypesecondByFirstId";
                        OkhttpHelper.getAllTypesecond(url,String.valueOf(firstId),new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(CategoryActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    typeSecondDatas = new ArrayList<>();
                                    for(int i = 0;i<jsonArray.length();i++) {
                                        JSONObject list = jsonArray.getJSONObject(i);
                                        JSONObject typesecond = list.getJSONObject("typesecond");
                                        JSONArray typethirds = list.getJSONArray("typethird");
                                        int secondId = typesecond.getInt("secondId");
                                        String secondName = typesecond.getString("secondName");
                                        Map map = new HashMap();
                                        map.put("secondId",secondId);
                                        map.put("secondName",secondName);
                                        typeThirdDatas = new ArrayList<>();
                                        for (int j=0;j<typethirds.length();j++){
                                            JSONObject typethird = typethirds.getJSONObject(j);
                                            int thirdId = typethird.getInt("thirdId");
                                            String thirdName = typethird.getString("thirdName");
                                            String thirdPicture = typethird.getString("thirdPicture");
                                            Map map1 = new HashMap();
                                            map1.put("thirdId",thirdId);
                                            map1.put("thirdName",thirdName);
                                            map1.put("thirdPicture",thirdPicture);
                                            typeThirdDatas.add(map1);
                                        }
                                        map.put("typeThirdDatas",typeThirdDatas);
                                        typeSecondDatas.add(map);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            typeSecondAdapter=new TypeSecondAdapter(CategoryActivity.this,typeSecondDatas);
                                            lv_typeSecond.setAdapter(typeSecondAdapter);

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
        });
    }

    /*private void gv_Click() {
        gv_typeThird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                *//*String thirdName = (String) typeThirdDatas.get(position).get("thirdName");
                Toast.makeText(CategoryActivity.this,thirdName,Toast.LENGTH_SHORT).show();*//*
                Intent intent = new Intent(CategoryActivity.this,TypeComActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) typeThirdDatas);
                startActivity(intent);
            }
        });
    }*/


    public void back(View v) throws Exception{
        this.finish();
    }
}

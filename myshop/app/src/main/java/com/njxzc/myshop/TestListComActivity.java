package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.ComAdapter;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TestListComActivity extends AppCompatActivity {

    private ListView list_com;
    private ArrayList<Map<String,Object>> datas;
    private ComAdapter comAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list_com);
        initCompoment();
        init();
    }

    private void initCompoment() {
        list_com = (ListView) findViewById(R.id.list_com);
    }

    private void init() {
        //1.获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = OkhttpHelper.getIp()+"/shopsystem/androidCom/list";
                OkhttpHelper.getAllCom(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestListComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try{
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            JSONArray jsonArray = new JSONArray(responseData);
                            datas = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject list = jsonArray.getJSONObject(i);
                                String comImageMain = list.getString("comImageMain");
                                String comName = list.getString("comName");
                                Double currentPrice = list.getDouble("currentPrice");
                                int hits = list.getInt("hits");
                                Map map = new HashMap();
                                map.put("comImageMain",comImageMain);
                                map.put("comName",comName);
                                map.put("currentPrice",currentPrice);
                                map.put("hits",hits);
                                datas.add(map);
                            }
                            System.out.println(datas);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    comAdapter = new ComAdapter(TestListComActivity.this,datas);
                                    list_com.setAdapter(comAdapter);
                                }
                            });



                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TestListComActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }


                });

            }
        }).start();

    }

    //重写onKeyDown方法 点击手机返回键可以回到登录界面
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(TestListComActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}

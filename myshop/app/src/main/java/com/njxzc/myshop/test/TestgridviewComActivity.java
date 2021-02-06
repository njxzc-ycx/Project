package com.njxzc.myshop.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.GridView;
import android.widget.Toast;

import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.TestListComActivity;
import com.njxzc.myshop.utils.ComAdapter;
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

public class TestgridviewComActivity extends AppCompatActivity {

    private GridView gv_listCom;
    private ArrayList<Map<String,Object>> datas;
    private ComAdapter comAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testgridview_com);
        initCompoment();
        init();
    }

    private void initCompoment() {
        gv_listCom = (GridView) findViewById(R.id.gv_listCom);
    }

    private void init() {
        //1.获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.0.103:8088/shopsystem/androidCom/list";
                OkhttpHelper.getRequest(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TestgridviewComActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
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
                                String school = user.getString("school");
                                String comImageMain = list.getString("comImageMain");
                                String comName = list.getString("comName");
                                Double currentPrice = list.getDouble("currentPrice");
                                int collects = list.getInt("collects");
                                Map map = new HashMap();
                                map.put("comImageMain",comImageMain);
                                map.put("comName",comName);
                                map.put("currentPrice",currentPrice);
                                map.put("collects",collects);
                                map.put("school",school);
                                datas.add(map);
                            }
                            System.out.println(datas);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    comAdapter = new ComAdapter(TestgridviewComActivity.this,datas);
                                    gv_listCom.setAdapter(comAdapter);
                                }
                            });



                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TestgridviewComActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(TestgridviewComActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}

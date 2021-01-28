package com.njxzc.myshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url="http://192.168.0.103:8088/shopsystem/androidCom/list";
                        OkhttpHelper.getRequest(url, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(MainActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try{
                                    String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    for(int i = 0;i<jsonArray.length();i++){
                                        JSONObject list = jsonArray.getJSONObject(i);
                                        Log.d("商品编号",""+list.getInt("comId"));
                                        Log.d("商品名",""+list.getString("comName"));
                                        Log.d("商品描述",""+list.getString("des"));
                                        Log.d("原价",""+list.getDouble("primePrice"));
                                        Log.d("现价",""+list.getDouble("currentPrice"));
                                        Log.d("数量",""+list.getInt("count"));
                                        JSONObject user = list.getJSONObject("user");
                                        Log.d("卖家",""+user.getString("realName"));
                                        JSONObject typethird = list.getJSONObject("typethird");
                                        Log.d("分类",""+typethird.getString("thirdName"));
                                        System.out.println("--------------------------------------------");

                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                }


                        });

                    }
                }).start();
            }
        });
    }

    public void toLogin(View v) throws Exception{
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }







}

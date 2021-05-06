package com.njxzc.myshop.personal_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.Tag;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.njxzc.myshop.LoginActivity;
import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.SameSchoolComAdapter;
import com.njxzc.myshop.utils.DealSchoolsJson;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UpdateSchoolActivity extends AppCompatActivity {
    private static ArrayList<String> datas;
    private ArrayList<String> citys;
    private ArrayList<String> schoolNames;
    private ListView lv_schools;
    private ListView lv_citys;
    private SharedPreferences sp;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_school);
        Model.getInstance().init(this);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        initView();
        initCity();
        initdatas();

    }

    private void initView() {
        lv_schools = (ListView) findViewById(R.id.lv_schools);
        lv_citys = (ListView) findViewById(R.id.lv_citys);
    }

    private void initCity() {
        //得到本地json文本内容
        String fileName = "province.json";
        String provinceJson = getJson(fileName,this);
        System.out.println(provinceJson);
        try {
            JSONObject jsonObject = new JSONObject(provinceJson);
            JSONArray list = jsonObject.getJSONArray("province");
            System.out.println(list);
            citys = new ArrayList<>();
            for(int i=0;i<list.length();i++) {
                String city = (String) list.get(i);
                citys.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.provinces_style,citys);
        lv_citys.setAdapter(adapter);
        lv_citys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    JSONObject jsonObject = new JSONObject(datas.get(i));
                    String city = citys.get(i);
                    String schools = jsonObject.getString(city);
                    System.out.println(schools);
                    JSONArray school = new JSONArray(schools);
                    schoolNames = new ArrayList<>();
                    for(int j=0;j<school.length();j++) {
                        String schoolEach = (String) school.get(j);
                        schoolNames.add(schoolEach);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> schoolAdapter = new ArrayAdapter<String>(UpdateSchoolActivity.this,R.layout.schools_style,schoolNames);
                lv_schools.setAdapter(schoolAdapter);
                schoolAdapter.notifyDataSetChanged();

                lv_schools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        final int userId = sp.getInt("userId",0);
                        final String schoolName = schoolNames.get(i);
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateSchoolActivity.this);
                        builder.setMessage("确定将学校修改为"+schoolName+"？");
                        builder.setTitle("提示");

                        //取消按钮
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        //确定按钮
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                //start the progress dialog
                                mProgressDialog = ProgressDialog.show(UpdateSchoolActivity.this, "", "正在提交...");
                                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(1000);
                                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidUser/insertSchool";
                                            OkhttpHelper.insertSchool(url, schoolName,String.valueOf(userId), new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(UpdateSchoolActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                                            // dismiss the progress dialog
                                                            mProgressDialog.dismiss();
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onResponse(Call call, Response response) throws IOException {
                                                    final String responseData = response.body().string();
                                                    System.out.println(responseData);
                                                    if(responseData.equals("insertSchoolFail")){
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(UpdateSchoolActivity.this, "学校绑定失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }else if(responseData.equals("insertSchoolSuccess")){
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(UpdateSchoolActivity.this, "学校绑定成功", Toast.LENGTH_SHORT).show();
                                                                SameSchoolComAdapter.count=6;
                                                                Intent intent = new Intent(UpdateSchoolActivity.this, MainActivity.class);
                                                                sp.edit().putString("school",schoolName).commit();
                                                                startActivity(intent);
                                                                UpdateSchoolActivity.this.finish();
                                                            }
                                                        });
                                                    }
                                                    // dismiss the progress dialog
                                                    mProgressDialog.dismiss();

                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        });
                        //显示对话框
                        builder.create().show();
                    }
                });
            }
        });


    }

    public void initdatas(){
        //得到本地json文本内容
        String fileName = "schools.json";
        String schoolJson = getJson(fileName,this);
        try {
            JSONObject jsonObject = new JSONObject(schoolJson);
            JSONArray list = jsonObject.getJSONArray("campus");
            datas = new ArrayList<>();
            for(int i=0;i<list.length();i++) {
                JSONObject city = list.getJSONObject(i);
                datas.add(city.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static String getJson(String fileName, Context context){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public void backPersonalInfo(View v) throws Exception{
        this.finish();
    }
}

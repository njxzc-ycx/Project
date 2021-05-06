package com.njxzc.myshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.adapter.AllAddressAdapter;
import com.njxzc.myshop.personal_activity.AddressesActivity;
import com.njxzc.myshop.personal_activity.UpdateAddressActivity;
import com.njxzc.myshop.personal_activity.addAddressActivity;
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

public class ChooseAddressActivity extends AppCompatActivity {
    private ListView lv_addresses;
    private ImageView iv_noAddress;
    private Button btn_addAddress;
    private AllAddressAdapter allAddressAdapter;
    private ArrayList<Map<String,Object>> addressDatas;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_address);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(this);
        initView();
        init();
        clickItem();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshAddress".equals(msg)){
                    init();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
    }

    private void initView() {
        lv_addresses = (ListView) findViewById(R.id.lv_addresses);
        btn_addAddress = (Button) findViewById(R.id.btn_addAddress);
        iv_noAddress = (ImageView) findViewById(R.id.iv_noAddress) ;
    }

    private void init() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url =  OkhttpHelper.getIp()+"/shopsystem/androidAddress/findAllAddressByUserId";
                OkhttpHelper.findAllAddressByUserId(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChooseAddressActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            final String responseData = response.body().string();
                            JSONArray jsonArray = new JSONArray(responseData);
                            addressDatas = new ArrayList<Map<String, Object>>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject address = jsonArray.getJSONObject(i);
                                int addressId = address.getInt("addressId");
                                int userId = address.getInt("userId");
                                int defaultAddress = address.getInt("defaultAddress");
                                String addressDetail = address.getString("address");
                                String linkPhone = address.getString("phone");
                                String linkMan = address.getString("linkman");
                                String area = address.getString("area");
                                String addressNote = address.getString("addressNote");
                                Map map = new HashMap();
                                map.put("addressId",addressId);
                                map.put("userId",userId);
                                map.put("defaultAddress",defaultAddress);
                                map.put("addressDetail",addressDetail);
                                map.put("area",area);
                                map.put("linkPhone",linkPhone);
                                map.put("linkMan",linkMan);
                                map.put("addressNote",addressNote);
                                addressDatas.add(map);
                            }
                            System.out.println(addressDatas);
                            if (addressDatas.size()>0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    allAddressAdapter = new AllAddressAdapter(ChooseAddressActivity.this,addressDatas);
                                    lv_addresses.setAdapter(allAddressAdapter);
                                    lv_addresses.setVisibility(View.VISIBLE);
                                    iv_noAddress.setVisibility(View.GONE);
                                }
                            });}else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lv_addresses.setVisibility(View.GONE);
                                        iv_noAddress.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });


    }


    public void toAddAddress(View v) throws Exception {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url =  OkhttpHelper.getIp()+"/shopsystem/androidAddress/findAllAddressCount";
                OkhttpHelper.findAllAddressCount(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChooseAddressActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseData = response.body().string();
                        int count = Integer.parseInt(responseData);
                        if (count<5){
                            Intent intent = new Intent(ChooseAddressActivity.this,addAddressActivity.class);
                            startActivity(intent);
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChooseAddressActivity.this,"地址最多只能有5个",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });


    }


    private void clickItem() {
        lv_addresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String area = (String) addressDatas.get(i).get("area");
                String linkman = (String) addressDatas.get(i).get("linkMan");
                String linkPhone = (String) addressDatas.get(i).get("linkPhone");
                String addressDetail = (String) addressDatas.get(i).get("addressDetail");
                int addressId = (int) addressDatas.get(i).get("addressId");
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("flag", "refreshAddress");
                intent.putExtra("area", area);
                intent.putExtra("addressDetail", addressDetail);
                intent.putExtra("linkPhone", linkPhone);
                intent.putExtra("linkman", linkman);
                intent.putExtra("addressId", addressId);
                LocalBroadcastManager.getInstance(ChooseAddressActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                ChooseAddressActivity.this.finish();

            }
        });
    }

    public void backPersonalInfo(View v) throws Exception{
        this.finish();
    }
}

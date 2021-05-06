package com.njxzc.myshop.MyOrdersFragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.OrderDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.MyBuysAdapter;
import com.njxzc.myshop.adapter.MySellsAdapter;
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

/**
 * Created by 殷晨旭 on 2021/3/12.
 */

public class MyBuysFragment extends android.support.v4.app.Fragment {

    private boolean firstLoad  = false;
    private ListView lv_myBuys;
    private MyBuysAdapter myBuysAdapter;
    private SwipeRefreshLayout sr_myBuys;
    private ArrayList<Map<String,Object>> datas;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_buys_style,null);
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Model.getInstance().init(getActivity());
        firstLoad = true;//视图创建完成，将变量置为true
        lv_myBuys = view.findViewById(R.id.lv_myBuys);
        sr_myBuys = view.findViewById(R.id.sr_myBuys);
        if (getUserVisibleHint()) {//判断Fragment是否可见

            //初始化数据
            initDatas();
            firstLoad = false;//将变量置为false

        }
        refresh();
        clickItem();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshOrder".equals(msg)){
                    initDatas();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firstLoad = false;//视图销毁将变量置为false
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (firstLoad && isVisibleToUser) {//视图变为可见并且是第一次加载
            //初始化数据
            initDatas();
            firstLoad = false;
        }
    }

    private void initDatas() {
        final int userId = sp.getInt("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String url= OkhttpHelper.getIp()+"/shopsystem/androidOrder/findBuyerOrder";
                OkhttpHelper.findBuyerOrder(url, String.valueOf(userId), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
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
                                JSONObject userOrder = list.getJSONObject(i);
                                JSONObject address = userOrder.getJSONObject("address");
                                JSONObject orderDetail = userOrder.getJSONObject("orderDetail");
                                JSONObject commodity = orderDetail.getJSONObject("commodity");
                                JSONObject user = commodity.getJSONObject("user");
                                int orderId = userOrder.getInt("orderId");
                                int userId = userOrder.getInt("userId");
                                String orderTime = userOrder.getString("orderTime");
                                String getTime = userOrder.getString("getTime");
                                Double total = userOrder.getDouble("total");
                                int status = userOrder.getInt("status");
                                int addressId = address.getInt("addressId");
                                String addressDetail = address.getString("address");
                                String area = address.getString("area");
                                String linkPhone = address.getString("phone");
                                String linkMan = address.getString("linkman");
                                int orderDetailId = orderDetail.getInt("orderDetailId");
                                int buyCount = orderDetail.getInt("buyCount");
                                String comName = commodity.getString("comName");
                                int sellerId = commodity.getInt("sellerId");
                                int comId = commodity.getInt("comId");
                                int flag = commodity.getInt("flag");
                                int count = commodity.getInt("count");
                                Double currentPrice = commodity.getDouble("currentPrice");
                                String comImageMain = commodity.getString("comImageMain");
                                String school = user.getString("school");
                                String nickName = user.getString("nickName");
                                String userName = user.getString("userName");
                                String profilePhoto = user.getString("profilePhoto");
                                int sex = user.getInt("sex");
                                Map map = new HashMap();
                                map.put("orderId",orderId);
                                map.put("userId",userId);
                                map.put("orderTime",orderTime);
                                map.put("count",count);
                                map.put("flag",flag);
                                map.put("getTime",getTime);
                                map.put("total",total);
                                map.put("status",status);
                                map.put("addressId",addressId);
                                map.put("addressDetail",addressDetail);
                                map.put("area",area);
                                map.put("linkPhone",linkPhone);
                                map.put("linkMan",linkMan);
                                map.put("orderDetailId",orderDetailId);
                                map.put("buyCount",buyCount);
                                map.put("comName",comName);
                                map.put("currentPrice",currentPrice);
                                map.put("sellerId",sellerId);
                                map.put("comId",comId);
                                map.put("comImageMain",comImageMain);
                                map.put("school",school);
                                map.put("nickName",nickName);
                                map.put("userName",userName);
                                map.put("profilePhoto",profilePhoto);
                                map.put("sex",sex);
                                datas.add(map);
                            }
                            System.out.println(datas);
                            if (!isDestroy(getActivity()) && isAdded()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myBuysAdapter = new MyBuysAdapter(getActivity(), datas);
                                        lv_myBuys.setAdapter(myBuysAdapter);
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

    protected boolean isDestroy(Activity activity) {
        return activity == null || activity.isFinishing() ||
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
    }

    private void clickItem() {
        lv_myBuys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),OrderDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
            }
        });

    }

    private void refresh() {
        sr_myBuys.setColorSchemeColors(Color.BLUE);
        sr_myBuys.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        sr_myBuys.setRefreshing(false);
                    }
                },2000);
            }
        });

    }
}

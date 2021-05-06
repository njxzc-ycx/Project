package com.njxzc.myshop.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.OrderDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.AllComAdapter;
import com.njxzc.myshop.adapter.MyComsAdapter;
import com.njxzc.myshop.adapter.PerComAdapter;
import com.njxzc.myshop.personal_activity.MyComsActivity;
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

/**
 * Created by 殷晨旭 on 2021/5/3.
 */

public class perComFragment extends android.support.v4.app.Fragment {
    private boolean firstLoad  = false;
    private int userId;
    private SwipeRefreshLayout sr_perCom;
    private GridView gv_perCom;
    private PerComAdapter perComAdapter;
    private ArrayList<Map<String,Object>> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.percom_style,null);
        Model.getInstance().init(getActivity());
        firstLoad = true;//视图创建完成，将变量置为true
        sr_perCom = view.findViewById(R.id.sr_perCom);
        gv_perCom = view.findViewById(R.id.gv_perCom);
        if (getUserVisibleHint()) {//判断Fragment是否可见
            //初始化数据
            initDatas();
            firstLoad = false;//将变量置为false
        }
        refresh();
        clickItem();

        return view;
    }

    private void initDatas() {
        userId = getActivity().getIntent().getIntExtra("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/findMyCommodityBySellerId";
                OkhttpHelper.getComs(url,String.valueOf(userId),new Callback() {
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
                        try{
                            String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                            System.out.println(responseData);
                            JSONArray jsonArray = new JSONArray(responseData);
                            datas = new ArrayList<>();
                            for(int i = 0;i<jsonArray.length();i++){
                                JSONObject list = jsonArray.getJSONObject(i);
                                JSONObject user = list.getJSONObject("user");
                                JSONObject typethird = list.getJSONObject("typethird");
                                String school = user.getString("school");
                                String thirdName = typethird.getString("thirdName");
                                String nickName = user.getString("nickName");
                                String profilePhoto = user.getString("profilePhoto");
                                String userName = user.getString("userName");
                                int sex = user.getInt("sex");
                                int thirdId = typethird.getInt("thirdId");
                                String comImageMain = list.getString("comImageMain");
                                String comImageOthers = list.getString("comImageOther");
                                String updateTime = list.getString("updateTime");
                                String onTime = list.getString("onTime");
                                String inTime = list.getString("inTime");
                                String comName = list.getString("comName");
                                int sellerId = list.getInt("sellerId");
                                int count = list.getInt("count");
                                int flag = list.getInt("flag");
                                int comId = list.getInt("comId");
                                int hits = list.getInt("hits");
                                String des = list.getString("des");
                                Double currentPrice = list.getDouble("currentPrice");
                                Double primePrice = list.getDouble("primePrice");
                                int isBargain = list.getInt("isBargain");
                                int collects = list.getInt("collects");
                                Map map = new HashMap();
                                map.put("comImageMain",comImageMain);
                                map.put("comName",comName);
                                map.put("userName",userName);
                                map.put("thirdId",thirdId);
                                map.put("thirdName",thirdName);
                                map.put("updateTime",updateTime);
                                map.put("count",count);
                                map.put("des",des);
                                map.put("hits",hits);
                                map.put("flag",flag);
                                map.put("comId",comId);
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    perComAdapter = new PerComAdapter(getActivity(),datas);
                                    gv_perCom.setAdapter(perComAdapter);


                                }
                            });

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                });

            }
        });



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

    private void clickItem() {
        gv_perCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(getActivity(),
                        "点击位置"+position+datas.get(position).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(getActivity(),ComDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
            }
        });

    }

    private void refresh() {
        sr_perCom.setColorSchemeColors(Color.BLUE);
        sr_perCom.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        sr_perCom.setRefreshing(false);
                    }
                },2000);
            }
        });

    }
}

package com.njxzc.myshop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.njxzc.myshop.DiscussDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.InteractAdapter;
import com.njxzc.myshop.adapter.PerComAdapter;
import com.njxzc.myshop.adapter.PerDiscussAdapter;
import com.njxzc.myshop.utils.GetStandardDate;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/5/3.
 */

public class perDisFragment extends android.support.v4.app.Fragment {
    private boolean firstLoad  = false;
    private int userId;
    private SwipeRefreshLayout sr_perDis;
    private PerDiscussAdapter perDiscussAdapter;
    private ListView lv_perDis;

    private ArrayList<Map<String,Object>> datas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.perdis_style,null);
        Model.getInstance().init(getActivity());
        firstLoad = true;//视图创建完成，将变量置为true
        sr_perDis = view.findViewById(R.id.sr_perDis);
        lv_perDis = view.findViewById(R.id.lv_perDis);

        if (getUserVisibleHint()) {//判断Fragment是否可见
            //初始化数据
            initDatas();
            firstLoad = false;//将变量置为false

        }
        clickItem();
        refresh();
        return view;
    }

    private void initDatas() {
        userId = getActivity().getIntent().getIntExtra("userId",0);
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/findDiscussByUserId";
                OkhttpHelper.getDiscussByUserId(url, String.valueOf(userId), new Callback() {
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
                            String responseData = response.body().string();
                            JSONArray list = new JSONArray(responseData);
                            datas = new ArrayList<Map<String, Object>>();
                            for (int i=0;i<list.length();i++){
                                JSONObject data = list.getJSONObject(i);
                                JSONObject user = data.getJSONObject("user");
                                int discussId = data.getInt("discussId");
                                String discussDes = data.getString("discussDes");
                                String discussImages = data.getString("discussImages");
                                String discussTime = data.getString("discussTime");
                                //将字符串类型转为date类型
                                Date time =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(discussTime);
                                //将时间戳转为 多少天前、多少小时前、几年前、几个月前
                                String tranDiscussTime = GetStandardDate.getTimeFormatText(time);
                                int discussUp = data.getInt("discussUp");
                                int discussComments = data.getInt("discussComments");
                                int discussHits = data.getInt("discussHits");
                                int userId = data.getInt("userId");
                                int isAnonymity = data.getInt("isAnonymity");
                                String nickName = user.getString("nickName");
                                String profilePhoto = user.getString("profilePhoto");
                                String school = user.getString("school");
                                int sex = user.getInt("sex");
                                Map map = new HashMap();
                                map.put("discussId",discussId);
                                map.put("discussDes",discussDes);
                                map.put("discussImages",discussImages);
                                map.put("discussTime",tranDiscussTime);
                                map.put("discussUp",discussUp);
                                map.put("isAnonymity",isAnonymity);
                                map.put("discussComments",discussComments);
                                map.put("discussHits",discussHits);
                                map.put("userId",userId);
                                map.put("nickName",nickName);
                                map.put("profilePhoto",profilePhoto);
                                map.put("school",school);
                                map.put("sex",sex);
                                datas.add(map);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    perDiscussAdapter = new PerDiscussAdapter(getActivity(),datas);
                                    lv_perDis.setAdapter(perDiscussAdapter);

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
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
        lv_perDis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int discussId = (int) datas.get(position).get("discussId");
                Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String url =  OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/addDisscussHits";
                            OkhttpHelper.addDisscussHits(url,String.valueOf(discussId),new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                }

                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Intent intent = new Intent(getActivity(),DiscussDetailActivity.class);
                intent.putExtra("discussId",discussId);
                startActivity(intent);
            }
        });


    }

    private void refresh() {
        sr_perDis.setColorSchemeColors(Color.BLUE);
        sr_perDis.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        sr_perDis.setRefreshing(false);
                    }
                },2000);
            }
        });

    }
}

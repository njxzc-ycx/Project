package com.njxzc.myshop.DiscussFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.njxzc.myshop.DiscussDetailActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.RequireAdapter;
import com.njxzc.myshop.customview.LoadMoreListView;
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
 * Created by 殷晨旭 on 2021/3/8.
 */

public class RequireFragment extends android.support.v4.app.Fragment {
    private boolean firstLoad  = false;
    private ArrayList<Map<String,Object>> discussDatas;
    private RequireAdapter requireAdapter;
    private LoadMoreListView ll_require;
    private SwipeRefreshLayout sr_discuss;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.require_discuss_fragment,null);
        Model.getInstance().init(getActivity());
        firstLoad = true;//视图创建完成，将变量置为true
        ll_require = view.findViewById(R.id.ll_require);
        sr_discuss = view.findViewById(R.id.sr_discuss);
        refresh();
        clickItem();

        if (getUserVisibleHint()) {//判断Fragment是否可见

            //初始化数据
            initDatas();
            firstLoad = false;//将变量置为false

        }


        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                int typeId = intent.getIntExtra("type",0);
                if("refreshDiscuss".equals(msg)&&typeId==1){
                    initDatas();
                    firstLoad = false;//将变量置为false
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);

        ll_require.setONLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                loadMore();
            }
        });

        return view;
    }

    private void loadMore() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requireAdapter.count += 6;
                        requireAdapter.notifyDataSetChanged();
                        ll_require.setLoadCompleted();
                    }
                });
            }
        }.start();

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

    //初始化数据
    private void initDatas() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                final String url= OkhttpHelper.getIp()+"/shopsystem/androidDiscuss/findDiscussByType";
                OkhttpHelper.getRequireDiscuss(url, new Callback() {
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
                            discussDatas = new ArrayList<Map<String, Object>>();
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
                                map.put("isAnonymity",isAnonymity);
                                map.put("discussImages",discussImages);
                                map.put("discussTime",tranDiscussTime);
                                map.put("discussUp",discussUp);
                                map.put("discussComments",discussComments);
                                map.put("discussHits",discussHits);
                                map.put("userId",userId);
                                map.put("nickName",nickName);
                                map.put("profilePhoto",profilePhoto);
                                map.put("school",school);
                                map.put("sex",sex);
                                discussDatas.add(map);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    requireAdapter = new RequireAdapter(getActivity(),discussDatas);
                                    ll_require.setAdapter(requireAdapter);

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

    private void clickItem() {
        ll_require.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final int discussId = (int) discussDatas.get(position).get("discussId");
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
        sr_discuss.setColorSchemeColors(Color.BLUE,Color.GREEN);
        sr_discuss.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initDatas();
                        sr_discuss.setRefreshing(false);
                    }
                },2000);
            }
        });

    }
}

package com.njxzc.myshop.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.R;

import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.njxzc.myshop.adapter.SameSchoolComAdapter;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.adapter.ComAdapter;
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
 * Created by 殷晨旭 on 2021/2/7.
 */
public class SameSchoolComFragment extends android.support.v4.app.Fragment {

    private boolean firstLoad  = false;
    private ArrayList<Map<String,Object>> datas;
    private SameSchoolComAdapter sameSchoolComAdapter;
    private PullToRefreshGridView mPullRefresh_listSameSchoolCom;
    /*private GridView mgv_listSameSchoolCom;*/
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sameschoolcom_fragment, container, false);
        mPullRefresh_listSameSchoolCom = view.findViewById(R.id.gv_listSameSchoolCom);
        Model.getInstance().init(getActivity());
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        /*mgv_listSameSchoolCom = mPullRefresh_listSameSchoolCom.getRefreshableView();*/

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefresh_listSameSchoolCom.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向下拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向上拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask2().execute();
            }

        });
        firstLoad = true;//视图创建完成，将变量置为true

        if (getUserVisibleHint()) {//判断Fragment是否可见
            //初始化数据
            init();
            firstLoad = false;//将变量置为false
        }

        clickItem();
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshCom".equals(msg)){
                    init();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);

        return view;
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return datas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            final String school = sp.getString("school","");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/listSameSchoolCom";
                    OkhttpHelper.getSameSchoolCom(url,school, new Callback() {
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
                                    String school = user.getString("school");
                                    String nickName = user.getString("nickName");
                                    String userName = user.getString("userName");
                                    String profilePhoto = user.getString("profilePhoto");
                                    int sex = user.getInt("sex");
                                    String comImageMain = list.getString("comImageMain");
                                    String comImageOthers = list.getString("comImageOther");
                                    String updateTime = list.getString("updateTime");
                                    String onTime = list.getString("onTime");
                                    String inTime = list.getString("inTime");
                                    String comName = list.getString("comName");
                                    int sellerId = list.getInt("sellerId");
                                    int counts = list.getInt("count");
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
                                    map.put("des",des);
                                    map.put("hits",hits);
                                    map.put("comId",comId);
                                    map.put("flag",flag);
                                    map.put("counts",counts);
                                    map.put("sellerId",sellerId);
                                    map.put("updateTime",updateTime);
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
                                        sameSchoolComAdapter.count=6;
                                        sameSchoolComAdapter = new SameSchoolComAdapter(getActivity(),datas);
                                        mPullRefresh_listSameSchoolCom.setAdapter(sameSchoolComAdapter);
                                        sameSchoolComAdapter.notifyDataSetChanged();
                                    }
                                });
                            }catch (Exception e){
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity(),"网络连接失败！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        }


                    });
                }
            });
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefresh_listSameSchoolCom.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private class GetDataTask2 extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return datas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            if(sameSchoolComAdapter.count==datas.size()){
                Toast.makeText(getActivity(),"没有更多数据了",Toast.LENGTH_SHORT).show();
            }
            sameSchoolComAdapter.count += 10;
            sameSchoolComAdapter.notifyDataSetChanged();


            // Call onRefreshComplete when the list has been refreshed.
            mPullRefresh_listSameSchoolCom.onRefreshComplete();

            super.onPostExecute(result);
        }
    }


    private void init() {
        final String school = sp.getString("school","");
        //1.获取数据
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidCom/listSameSchoolCom";
                OkhttpHelper.getSameSchoolCom(url,school,new Callback() {
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
                                String school = user.getString("school");
                                String nickName = user.getString("nickName");
                                String profilePhoto = user.getString("profilePhoto");
                                int sex = user.getInt("sex");
                                String userName = user.getString("userName");
                                String comImageMain = list.getString("comImageMain");
                                String comImageOthers = list.getString("comImageOther");
                                String updateTime = list.getString("updateTime");
                                String onTime = list.getString("onTime");
                                String inTime = list.getString("inTime");
                                String comName = list.getString("comName");
                                int sellerId = list.getInt("sellerId");
                                int comId = list.getInt("comId");
                                int flag = list.getInt("flag");
                                int counts = list.getInt("count");
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
                                map.put("des",des);
                                map.put("hits",hits);
                                map.put("counts",counts);
                                map.put("flag",flag);
                                map.put("comId",comId);
                                map.put("sellerId",sellerId);
                                map.put("updateTime",updateTime);
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
                                    sameSchoolComAdapter.count=6;
                                    sameSchoolComAdapter = new SameSchoolComAdapter(getActivity(),datas);
                                    mPullRefresh_listSameSchoolCom.setAdapter(sameSchoolComAdapter);
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(),"网络连接失败！",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }


                });
            }
        });
    }

    private void clickItem() {
        mPullRefresh_listSameSchoolCom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                /*Toast.makeText(getActivity(),
                        "点击位置"+position+sameSchoolComdatas.get(position).get("comName"),
                        Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(getActivity(),ComDetailActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("datas",(Serializable) datas);
                startActivity(intent);
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
            init();
            firstLoad = false;
        }
    }
}

package com.njxzc.myshop.test;

/**
 * Created by 殷晨旭 on 2021/2/7.
 */
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.njxzc.myshop.R;

import android.widget.GridView;
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

/**
 * Created by 殷晨旭 on 2021/2/7.
 */
public class TestFreshAllComFragment extends android.support.v4.app.Fragment {

    private ArrayList<Map<String,Object>> datas;
    private ComAdapter comAdapter;
    private PullToRefreshGridView mPullRefreshGridView;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.testfresh_all_com, container, false);
        mPullRefreshGridView = view.findViewById(R.id.testgv_listAllCom);
        mGridView = mPullRefreshGridView.getRefreshableView();

        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向下拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                /*Toast.makeText(getActivity(), "向上拉!", Toast.LENGTH_SHORT).show();*/
                new GetDataTask().execute();
            }

        });

        init();
        return view;
    }

    private class GetDataTask extends AsyncTask<Void, Void, ArrayList<Map<String,Object>>> {

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return datas;
        }

        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url="http://192.168.0.103:8088/shopsystem/androidCom/list";
                    OkhttpHelper.getAllCom(url, new Callback() {
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
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        comAdapter = new ComAdapter(getActivity(),datas);
                                        mPullRefreshGridView.setAdapter(comAdapter);
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
            }).start();
            comAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private void init() {
        //1.获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.0.103:8088/shopsystem/androidCom/list";
                OkhttpHelper.getAllCom(url, new Callback() {
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    comAdapter = new ComAdapter(getActivity(),datas);
                                    mPullRefreshGridView.setAdapter(comAdapter);
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
        }).start();

    }


}

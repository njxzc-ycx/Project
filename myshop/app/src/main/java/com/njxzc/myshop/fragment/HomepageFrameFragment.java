package com.njxzc.myshop.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.njxzc.myshop.MainActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.test.TestgridviewComActivity;
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

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 主页框架Fragment
 */

public class HomepageFrameFragment extends BaseFragment {

    private static  final String TAG = HomepageFrameFragment.class.getSimpleName();//"HomepageFrameFragment"
    private GridView gv_listCom;
    private ArrayList<Map<String,Object>> datas;
    private ComAdapter comAdapter;

    @Override
    protected View initView() {
        Log.e(TAG,"主页框架Fragment页面被初始化了...");
        gv_listCom = new GridView(mContext);
        gv_listCom.setNumColumns(2);
        gv_listCom.setVerticalSpacing(10);
        gv_listCom.setHorizontalSpacing(10);
        return gv_listCom;
    }

    @Override
    protected void initData() {
        super.initData();
        //1.获取数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://192.168.0.103:8088/shopsystem/androidCom/list";
                OkhttpHelper.getRequest(url, new Callback() {
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
                                    gv_listCom.setAdapter(comAdapter);
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

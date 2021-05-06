package com.njxzc.myshop.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.njxzc.myshop.CategoryActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.ResetPwdActivity;
import com.njxzc.myshop.SearchActivity;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.adapter.LooperPagerAdapter;
import com.njxzc.myshop.adapter.MyFragmentPagerAdapter;
import com.njxzc.myshop.customview.MyViewPager;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 主页框架Fragment
 */

public class HomepageFrameFragment extends BaseFragment implements AMapLocationListener, MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener {

    private static  final String TAG = HomepageFrameFragment.class.getSimpleName();//"HomepageFrameFragment"

    private String[] titles = {"闲置物品","同校物品"};

    private List<Fragment> data;

    private ViewPager viewPager;

    private MyViewPager mLoopPager;

    private LinearLayout mPointContainer;

    private LooperPagerAdapter mLooperPagerAdapter;

    private static List<Integer> sPics = new ArrayList<>();

    private List<String> slideshowDatas;

    static {
        sPics.add(R.drawable.source1);
        sPics.add(R.drawable.source2);
        sPics.add(R.drawable.source3);
    }

    private Handler mHandler = new Handler();
    private boolean mIsTouch = false;


    private TabLayout tabLayout;

    private FragmentPagerAdapter fragmentPagerAdapter;

    private static final int MY_PERMISSIONS_REQUEST_CALL_LOCATION = 1;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    private TextView tv_locationaddress;

    private ImageView iv_search;
    private TextView tv_search;

    private TextView tv_category;


    @Override
    protected View initView() {
        Log.e(TAG,"主页框架Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_homepage_frame,null);
        viewPager = view.findViewById(R.id.viewpaper);
        tabLayout = view.findViewById(R.id.tab_layout);
        mPointContainer = view.findViewById(R.id.points_container);
        mLoopPager = view.findViewById(R.id.looper_pager);
        mLoopPager.addOnPageChangeListener(this);

        mHandler.postDelayed(mLooperTask,3000);
        mLoopPager.setOnViewPagerTouchListener(this);
        /*iv_search = view.findViewById(R.id.iv_search);*/
        tv_search = view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    startActivity(intent);

            }
        });

        tv_category = view.findViewById(R.id.tv_category);
        tv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CategoryActivity.class);
                startActivity(intent);
            }
        });


        //初始化Fragment
        initFragment();
        tv_locationaddress = view.findViewById(R.id.tv_locationaddress);
        tv_locationaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检查版本是否大于M
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_CALL_LOCATION);
                        Toast.makeText(getActivity(),"请开启'定位服务'来允许'淘校园'确定您的位置",Toast.LENGTH_SHORT).show();
                    } else {
                        tv_locationaddress.setText("正在定位");
                        //"权限已申请";
                        showLocation();
                    }
                }
            }
        });
        return view;
    }

    private void insertPoint() {
        for (int i = 0; i < slideshowDatas.size(); i++) {
            View point = new View(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15,15);
            point.setBackground(getResources().getDrawable(R.drawable.shap_point_normal));
            layoutParams.leftMargin=20;
            layoutParams.topMargin=3;
            point.setLayoutParams(layoutParams);
            mPointContainer.addView(point);
            setSelectPoint(0);
        }

    }


    private void initFragment() {
        //初始化需要显示的所有界面
        data = new ArrayList<>();
        data.add(new AllComFragment());
        data.add(new SameSchoolComFragment());
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),data,titles);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void initData() {
        super.initData();

        Model.getInstance().init(getContext());
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                String url= OkhttpHelper.getIp()+"/shopsystem/androidSlideshow/findSlideshows";
                OkhttpHelper.findSlideshows(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();//获取后端返回过来的JSON格式结果
                        try {
                            JSONArray jsonArray = new JSONArray(responseData);
                            slideshowDatas = new ArrayList<String>();
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject list = jsonArray.getJSONObject(i);
                                String url = list.getString("slideshowUrl");
                                slideshowDatas.add(i,url);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //设置轮播图适配器
                                    mLooperPagerAdapter = new LooperPagerAdapter();
                                    mLooperPagerAdapter.setData(getActivity(),slideshowDatas);
                                    mLoopPager.setAdapter(mLooperPagerAdapter);
                                    //设置初始在100位置
                                    mLoopPager.setCurrentItem(100,false);
                                    //根据图片的个数去添加点的个数
                                    insertPoint();
                                }
                            });


                        } catch (JSONException e) {
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

        //检查版本是否大于M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_CALL_LOCATION);
            } else {
                //"权限已申请";
                showLocation();
            }
        }

    }

    @Override
    public void onPagerTouch(boolean isTouch) {
        mIsTouch = isTouch;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        /*mHandler.postDelayed(mLooperTask,3000);*/
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(mLooperTask);
    }

    private  Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if(!mIsTouch){
            //切换viewPager里的图片
            int currentItem = mLoopPager.getCurrentItem();
            mLoopPager.setCurrentItem(++currentItem,true);
            }
            mHandler.postDelayed(mLooperTask,3000);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //"权限已申请"
                showLocation();
            } else {
                showToast("权限已拒绝,不能定位");

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void showLocation() {
        try {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {

        }
    }

    //通过创造接口类对象的方法实现实现AMapLocationListener接口
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        try {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    //获取当前定位结果来源，如网络定位结果，详见定位类型表
                    /*Log.i("定位类型", amapLocation.getLocationType() + "");
                    Log.i("获取纬度", amapLocation.getLatitude() + "");
                    Log.i("获取经度", amapLocation.getLongitude() + "");
                    Log.i("获取精度信息", amapLocation.getAccuracy() + "");

                    //如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    Log.i("地址", amapLocation.getAddress());
                    Log.i("国家信息", amapLocation.getCountry());
                    Log.i("省信息", amapLocation.getProvince());
                    Log.i("城市信息", amapLocation.getCity());
                    Log.i("城区信息", amapLocation.getDistrict());
                    Log.i("街道信息", amapLocation.getStreet());
                    Log.i("街道门牌号信息", amapLocation.getStreetNum());
                    Log.i("城市编码", amapLocation.getCityCode());
                    Log.i("地区编码", amapLocation.getAdCode());
                    Log.i("获取当前定位点的AOI信息", amapLocation.getAoiName());
                    Log.i("获取当前室内定位的建筑物Id", amapLocation.getBuildingId());
                    Log.i("获取当前室内定位的楼层", amapLocation.getFloor());
                    Log.i("获取GPS的当前状态", amapLocation.getGpsAccuracyStatus() + "");

                    //获取定位时间
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());

                    Log.i("获取定位时间", df.format(date));*/
                    /*String city = amapLocation.getCity();
                    String district = amapLocation.getDistrict();
                    tv_locationaddress.setText(city+district);*/
                    tv_locationaddress.setText(amapLocation.getAoiName());

                    // 停止定位
                    mlocationClient.stopLocation();
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        } catch (Exception e) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        // 停止定位
        if (null != mlocationClient) {
            mlocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
    }

    @Override
    public void onDestroy() {
        destroyLocation();
        super.onDestroy();
    }

    private void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //这个方法的调用其实是viewPager停下来以后选中的位置
        int realPosition;
        if(mLooperPagerAdapter.getDataRealSize() != 0){
            realPosition = position % mLooperPagerAdapter.getDataRealSize();
        }else {
            realPosition = 0;
        }
        setSelectPoint(realPosition);



    }

    private void setSelectPoint(int realPosition) {
        for (int i = 0; i < mPointContainer.getChildCount(); i++) {
            View point = mPointContainer.getChildAt(i);
            if(i!=realPosition){
                //那就是白色
                point.setBackgroundResource(R.drawable.shap_point_normal);
            }else {
                //选中的颜色
                point.setBackgroundResource(R.drawable.shap_point_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}

package com.njxzc.myshop.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.njxzc.myshop.R;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.personal_activity.AboutusActivity;
import com.njxzc.myshop.personal_activity.HelpCenterActivity;
import com.njxzc.myshop.personal_activity.ServiceActivity;
import com.njxzc.myshop.personal_activity.SettingsActivity;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.utils.PersonalFrameFragmentAdapter;

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 我的框架Fragment
 */

public class PersonalFrameFragment extends BaseFragment {

    private static  final String TAG = PersonalFrameFragment.class.getSimpleName();//"PersonalFrameFragment"
    private PersonalFrameFragmentAdapter adapter;
    private ListView lv_personal;
    private TextView tv_nikeName;
    private TextView tv_mschool;
    private ImageView iv_photo;
    private RelativeLayout rl_userInfo;
    private String[] datas;
    private int[] images;
    AppInfo appInfo=new AppInfo();
    User user = new User();


    @Override
    protected View initView() {
        Log.e(TAG,"我的框架Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_personal_frame,null);
        tv_nikeName = view.findViewById(R.id.tv_nikeName);
        tv_mschool = view.findViewById(R.id.tv_mschool);
        iv_photo = view.findViewById(R.id.iv_photo);
        lv_personal = view.findViewById(R.id.lv_personal);
        lv_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = datas[position];
                if (data.equals("帮助中心")){
                    Intent intent = new Intent(mContext,HelpCenterActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("联系客服")){
                    Intent intent = new Intent(mContext,ServiceActivity.class);
                    mContext.startActivity(intent);
                }else if (data.equals("关于我们")){
                    Intent intent = new Intent(mContext,AboutusActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("设置")){
                    Intent intent = new Intent(mContext,SettingsActivity.class);
                    mContext.startActivity(intent);
                }
                Toast.makeText(mContext,"data=="+data,Toast.LENGTH_SHORT).show();
            }
        });

        rl_userInfo = view.findViewById(R.id.rl_userInfo);
        rl_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"进入个人中心",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"我的框架Fragment数据被初始化了...");
        appInfo = (AppInfo) getActivity().getApplication();
        user=appInfo.getUser();
        tv_nikeName.setText(user.getNickName());
        tv_mschool.setText(user.getSchool());
        Glide.with(mContext)
                .load(user.getProfilePhoto())
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(iv_photo);
        datas = new String[]{"我的二手","我的订单","我的收藏","帮助中心","联系客服","关于我们","设置"};
        images = new int[]{R.drawable.mygoods,R.drawable.myorders,R.drawable.mycollects,R.drawable.hlepcenter,R.drawable.service,R.drawable.aboutus,R.drawable.settings};
        //设置适配器
        adapter = new PersonalFrameFragmentAdapter(mContext,datas,images);
        lv_personal.setAdapter(adapter);
    }
}

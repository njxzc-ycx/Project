package com.njxzc.myshop.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.njxzc.myshop.R;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.personal_activity.AboutusActivity;
import com.njxzc.myshop.personal_activity.HelpCenterActivity;
import com.njxzc.myshop.personal_activity.MyCollectsActivity;
import com.njxzc.myshop.personal_activity.MyComsActivity;
import com.njxzc.myshop.personal_activity.MyDiscussActivity;
import com.njxzc.myshop.personal_activity.MyFollowsActivity;
import com.njxzc.myshop.personal_activity.MyOrdersActivity;
import com.njxzc.myshop.personal_activity.PersonalInfoActivity;
import com.njxzc.myshop.personal_activity.ServiceActivity;
import com.njxzc.myshop.personal_activity.SettingsActivity;
import com.njxzc.myshop.pojo.AppInfo;
import com.njxzc.myshop.pojo.User;
import com.njxzc.myshop.adapter.PersonalFrameFragmentAdapter;

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
    private SharedPreferences sp;


    @Override
    protected View initView() {
        Log.e(TAG,"我的框架Fragment页面被初始化了...");
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        View view = View.inflate(mContext, R.layout.fragment_personal_frame,null);
        tv_nikeName = view.findViewById(R.id.tv_nikeName);
        tv_mschool = view.findViewById(R.id.tv_mschool);
        iv_photo = view.findViewById(R.id.iv_photo);
        lv_personal = view.findViewById(R.id.lv_personal);
        lv_personal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String data = datas[position];
                if(data.equals("我的二手")){
                    Intent intent = new Intent(mContext,MyComsActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("我的交易")){
                    Intent intent = new Intent(mContext,MyOrdersActivity.class);
                    mContext.startActivity(intent);
                }
                else if(data.equals("我的收藏")){
                    Intent intent = new Intent(mContext,MyCollectsActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("我的关注")){
                    Intent intent = new Intent(mContext,MyFollowsActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("我的帖子")){
                    Intent intent = new Intent(mContext,MyDiscussActivity.class);
                    mContext.startActivity(intent);
                }else if(data.equals("帮助中心")){
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
                /*Toast.makeText(mContext,"data=="+data,Toast.LENGTH_SHORT).show();*/
            }
        });

        rl_userInfo = view.findViewById(R.id.rl_userInfo);
        rl_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(mContext,"个人信息",Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(mContext,PersonalInfoActivity.class);
                mContext.startActivity(intent);
            }
        });

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("flag");
                if("refreshNickName".equals(msg)){
                    initData();
                }else if ("refreshProfilePhoto".equals(msg)){
                    initData();
                }
            }
        };
        broadcastManager.registerReceiver(receiver, intentFilter);

        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"我的框架Fragment数据被初始化了...");
        String nickName = sp.getString("nickName","");
        String school = sp.getString("school","");
        String profilePhoto = sp.getString("profilePhoto","");
        tv_nikeName.setText(nickName);
        tv_mschool.setText(school);
        //在使用Glide加载图片前，先进行Activity是否Destroy的判断
        if(!isDestroy((Activity)mContext)) {
            Glide.with(mContext)
                    .load(profilePhoto).asBitmap()
                /*.apply(RequestOptions.bitmapTransform(new CircleCrop()).error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))*/
                    .error(R.drawable.errorpic)
                    .placeholder(R.drawable.defaultpic)
                    .into(iv_photo);
        }
        datas = new String[]{"我的二手","我的交易","我的收藏","我的关注","我的帖子","帮助中心","联系客服","关于我们","设置"};
        images = new int[]{R.drawable.mygoods,R.drawable.myorders,R.drawable.mycollects,R.drawable.follow,R.drawable.mydiscuss,R.drawable.hlepcenter,R.drawable.service,R.drawable.aboutus,R.drawable.settings};
        //设置适配器
        adapter = new PersonalFrameFragmentAdapter(mContext,datas,images);
        lv_personal.setAdapter(adapter);
    }

    /**
     * 判断Activity是否Destroy
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity== null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}

package com.njxzc.myshop.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.njxzc.myshop.DiscussFragment.InteractFragment;
import com.njxzc.myshop.DiscussFragment.RequireFragment;
import com.njxzc.myshop.PublishDisActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.MyFragmentPagerAdapter;
import com.njxzc.myshop.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 发现框架Fragment
 */

public class DiscussFrameFragment extends BaseFragment {

    private static  final String TAG = DiscussFrameFragment.class.getSimpleName();//"DiscussFrameFragment"
    private String[] titles = {"求二手","来互动"};
    private List<Fragment> data;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ImageView iv_publishDis;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_discuss_frame,null);
        viewPager = view.findViewById(R.id.viewpaper);
        tabLayout = view.findViewById(R.id.tab_layout);
        iv_publishDis = view.findViewById(R.id.iv_publishDis);
        iv_publishDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),PublishDisActivity.class);
                startActivity(intent);
            }
        });
        //初始化Fragment
        initFragment();

        return view;
    }

    private void initFragment() {
        //初始化需要显示的所有界面
        data = new ArrayList<>();
        data.add(new RequireFragment());
        data.add(new InteractFragment());
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(),data,titles);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"发现框架Fragment数据被初始化了...");

    }
}

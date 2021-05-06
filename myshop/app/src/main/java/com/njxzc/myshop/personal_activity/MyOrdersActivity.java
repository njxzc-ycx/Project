package com.njxzc.myshop.personal_activity;

import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.njxzc.myshop.DiscussFragment.InteractFragment;
import com.njxzc.myshop.DiscussFragment.RequireFragment;
import com.njxzc.myshop.MyOrdersFragment.MyBuysFragment;
import com.njxzc.myshop.MyOrdersFragment.MySellsFragment;
import com.njxzc.myshop.R;
import com.njxzc.myshop.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    private String[] titles = {"我买到的","我卖出的"};
    private List<Fragment> data;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        initView();
        //初始化Fragment
        initFragment();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpaper);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

    }

    private void initFragment() {
        //初始化需要显示的所有界面
        data = new ArrayList<>();
        data.add(new MyBuysFragment());
        data.add(new MySellsFragment());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentPagerAdapter = new MyFragmentPagerAdapter(fragmentManager,data,titles);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }



    public void backPersonalFragment(View v) throws Exception{
        this.finish();
    }

}

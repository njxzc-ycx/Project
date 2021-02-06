package com.njxzc.myshop.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.njxzc.myshop.base.BaseFragment;

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 收藏框架Fragment
 */

public class CollectFrameFragment extends BaseFragment {

    private static  final String TAG = CollectFrameFragment.class.getSimpleName();//"CollectFrameFragment"
    private TextView textView;

    @Override
    protected View initView() {
        Log.e(TAG,"收藏框架Fragment页面被初始化了...");
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG,"收藏框架Fragment数据被初始化了...");
        textView.setText("收藏框架页面");
    }
}

package com.njxzc.myshop.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njxzc.myshop.R;

/**
 * Created by 殷晨旭 on 2021/2/6.
 */

public class HelpCenterAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] mHelps;

    public HelpCenterAdapter(Context context, String[] mHelps){
        this.mContext = context;
        this.mHelps = mHelps;

    }

    @Override
    public int getCount() {
        return mHelps.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        //通过样式文件创建view
        view = layoutInflater.inflate(R.layout.helpcenter_style,null);
        TextView helpstext = view.findViewById(R.id.tv_helpstext);
        //根据view对象查找样式文件中需要显示的组件
        helpstext.setText(mHelps[position]);
        return view;
    }
}

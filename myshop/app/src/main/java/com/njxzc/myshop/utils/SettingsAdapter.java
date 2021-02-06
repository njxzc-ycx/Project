package com.njxzc.myshop.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.R;

/**
 * Created by 殷晨旭 on 2021/2/6.
 */

public class SettingsAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mSettext;
    private final String[] mSetvalue;

    public SettingsAdapter(Context context, String[] mSettext, String[] mSetvalue){
        this.mContext = context;
        this.mSettext = mSettext;
        this.mSetvalue = mSetvalue;
    }

    @Override
    public int getCount() {
        return mSettext.length;
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
        view = layoutInflater.inflate(R.layout.settings_style,null);
        TextView settext = view.findViewById(R.id.tv_settext);
        TextView setvalue = view.findViewById(R.id.tv_setvalue);
        //根据view对象查找样式文件中需要显示的组件
        settext.setText(mSettext[position]);
        setvalue.setText(mSetvalue[position]);
        return view;
    }
}

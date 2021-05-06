package com.njxzc.myshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.njxzc.myshop.R;

/**
 * Created by 殷晨旭 on 2021/2/5.
 */

public class PersonalFrameFragmentAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] mDatas;
    private final int[] mImages;

    public PersonalFrameFragmentAdapter(Context context, String[] datas, int[] images){
        this.mContext = context;
        this.mDatas = datas;
        this.mImages = images;
    }

    @Override
    public int getCount() {
        return mDatas.length;
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
        view = layoutInflater.inflate(R.layout.lv_personal_style,null);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);
        //根据view对象查找样式文件中需要显示的组件
        Glide.with(mContext)
                .load(mImages[position])
                .into(imageView);
        textView.setText(mDatas[position]);
        return view;
    }
}

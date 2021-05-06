package com.njxzc.myshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.njxzc.myshop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class SchoolsAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> data;


    public SchoolsAdapter(Context context, ArrayList<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.schools_style,null);
        //根据view对象查找样式文件中需要显示的组件
        TextView school = (TextView)view.findViewById(R.id.tv_school);
        //取出每一行需要显示的数据
        String schoolName = data.get(position);
        System.out.println(schoolName);
        school.setText(schoolName);


        return view;
    }
}

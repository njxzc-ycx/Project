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

import com.njxzc.myshop.CategoryActivity;
import com.njxzc.myshop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class TypeFirstAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;
    private int selectedPosition = -1;



    public TypeFirstAdapter(Context context, List<Map<String,Object>> data){
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

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.typefirst_style,null);
        //根据view对象查找样式文件中需要显示的组件
        TextView typefirst = (TextView)view.findViewById(R.id.tv_typeFirst);
        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String firstName = (String)hm.get("firstName");
        typefirst.setText(firstName);
        if (selectedPosition == position) {
            typefirst.setSelected(true);
            typefirst.setPressed(true);
            typefirst.setBackgroundColor(Color.WHITE);
        } else {
            typefirst.setSelected(false);
            typefirst.setPressed(false);
            typefirst.setBackgroundColor(Color.TRANSPARENT);
        }
        return view;
    }
}

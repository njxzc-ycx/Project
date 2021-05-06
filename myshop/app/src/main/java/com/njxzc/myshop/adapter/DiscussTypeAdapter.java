package com.njxzc.myshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njxzc.myshop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class DiscussTypeAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public DiscussTypeAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.discusstype_style,null);
        //根据view对象查找样式文件中需要显示的组件
        TextView tv_type = (TextView)view.findViewById(R.id.tv_type);
        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String discussTypeName = (String)hm.get("discussTypeName");

        //把显示的值赋给组件
        tv_type.setText(discussTypeName);


        return view;
    }
}

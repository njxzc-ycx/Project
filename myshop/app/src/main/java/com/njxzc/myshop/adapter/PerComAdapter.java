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

import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class PerComAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public PerComAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.percoms_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView comimagemain = (ImageView)view.findViewById(R.id.comImageMain);
        comimagemain.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView comname = (TextView)view.findViewById(R.id.comName);
        TextView currentprice = (TextView)view.findViewById(R.id.currentPrice);
        TextView collect = (TextView)view.findViewById(R.id.collects);
        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String comName = (String)hm.get("comName");
        Double currentPrice = (Double) hm.get("currentPrice");
        Integer collects = (Integer) hm.get("collects");
        String comImageMain = (String)hm.get("comImageMain");
        String schools = (String)hm.get("school");
        //把显示的值赋给组件
        comname.setText(comName);
        currentprice.setText(String.valueOf(currentPrice));
        collect.setText(String.valueOf(collects));
        Glide.with(context)
                .load(comImageMain).asBitmap()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(comimagemain);



        return view;
    }
}

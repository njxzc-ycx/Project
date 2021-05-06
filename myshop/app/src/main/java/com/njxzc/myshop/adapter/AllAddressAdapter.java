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

public class AllAddressAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public AllAddressAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.addresses_style,null);
        //根据view对象查找样式文件中需要显示的组件
        TextView tv_linkman = (TextView)view.findViewById(R.id.tv_linkman);
        TextView tv_linkphone = (TextView)view.findViewById(R.id.tv_linkphone);
        TextView tv_addressDetail = (TextView)view.findViewById(R.id.tv_addressDetail);
        TextView tv_ifdefault = (TextView)view.findViewById(R.id.tv_ifdefault);
        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String linkman = (String)hm.get("linkMan");
        String area = (String)hm.get("area");
        String linkPhone = (String)hm.get("linkPhone");
        String addressDetail = (String)hm.get("addressDetail");
        int defaultAddress = (int)hm.get("defaultAddress");

        //把显示的值赋给组件
        tv_linkman.setText(linkman);
        tv_linkphone.setText(linkPhone);
        tv_addressDetail.setText(area+" "+addressDetail);
        if(defaultAddress!=1){
            tv_ifdefault.setVisibility(View.GONE);
        }


        return view;
    }
}

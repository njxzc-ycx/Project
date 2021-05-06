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

public class TypeThirdAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;


    public TypeThirdAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.typethird_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView iv_typethird = view.findViewById(R.id.iv_typethird);
        TextView tv_typethird = view.findViewById(R.id.tv_typethird);
        iv_typethird.setScaleType(ImageView.ScaleType.FIT_XY);

        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String ivtypethird= (String)hm.get("thirdPicture");
        String tvtypethird = (String)hm.get("thirdName");
        //把显示的值赋给组件
        tv_typethird.setText(tvtypethird);
        //显示图片
        //glide 为4.6.1版本
        Glide.with(context)
                .load(ivtypethird).asBitmap()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_typethird);

        /*
        glide 为3.6版本
        Glide.with(context)
                .load(comImageMain)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .placeholder(R.drawable.defaultpic)//默认加载图片
                .error(R.drawable.errorpic)//加载出错图片
                .animate(R.anim.item_alpha_in)//加载动画
                .into(comimagemain);*/


        return view;
    }
}

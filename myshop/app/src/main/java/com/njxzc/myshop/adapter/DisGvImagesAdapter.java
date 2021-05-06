package com.njxzc.myshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.njxzc.myshop.PublishComActivity;
import com.njxzc.myshop.R;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class DisGvImagesAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> data;


    public DisGvImagesAdapter(Context context, ArrayList data){
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.discuss_images_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView iv_image = (ImageView)view.findViewById(R.id.iv_image);
        iv_image.setScaleType(ImageView.ScaleType.FIT_XY);
        String image = data.get(position);
            Glide.with(context)
                    .load(image).asBitmap()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                    .error(R.drawable.errorpic)
                    .placeholder(R.drawable.defaultpic)
                    .into(iv_image);


        return view;
    }
}

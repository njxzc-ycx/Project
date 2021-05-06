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
import com.njxzc.myshop.PublishDisActivity;
import com.njxzc.myshop.R;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class ListComPicsAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<String> data;


    public ListComPicsAdapter(Context context, ArrayList data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size()+1;

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
        View view = layoutInflater.inflate(R.layout.listpics_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView pics = (ImageView)view.findViewById(R.id.iv_eachPic);
        Button bt_del = (Button) view.findViewById(R.id.bt_del);
        pics.setScaleType(ImageView.ScaleType.FIT_XY);
        if (position < data.size()) {
            bt_del.setVisibility(View.VISIBLE);
            //取出每一行需要显示的数据
            String pic = data.get(position);
            final File file = new File(pic);
            //把显示的值赋给组
            //显示图片
            Glide.with(context)
                    .load(file)
                    .priority(Priority.HIGH)
                    .into(pics);
            bt_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    data.remove(position);
                    PublishComActivity.count += 1;
                    notifyDataSetChanged();
                    System.out.println(data);
                }
            });
        }else if(position>5){
            bt_del.setVisibility(View.GONE);
            pics.setVisibility(View.GONE);
        }
        else {
            pics.setImageResource(R.drawable.image_add);
            bt_del.setVisibility(View.GONE);
            pics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PhotoPicker.builder()
                            .setPhotoCount(PublishComActivity.count)
                            .setShowCamera(true)
                            .setShowGif(true)
                            .setPreviewEnabled(false)
                            .start((Activity) context, PhotoPicker.REQUEST_CODE);
                }
            });
        }
        return view;
    }
}

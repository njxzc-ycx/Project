package com.njxzc.myshop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.njxzc.myshop.R;

import java.util.List;

import cn.smssdk.gui.GroupListView;

/**
 * Created by 殷晨旭 on 2021/2/8.
 */

public class LooperPagerAdapter extends PagerAdapter {
    private List<String> mPics = null;
    private Context context;

    public void setData(Context context,List<String> pics) {
        this.mPics = pics;
        this.context = context;

    }

    @Override
    public int getCount() {
        if(mPics != null ){
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int realPosition = position % mPics.size();
        RoundedImageView imageView = new RoundedImageView(container.getContext());
        //imageView.setBackgroundColor(mColors.get(position));
        //设置完颜色数据以后，就添加到容器
        imageView.setCornerRadius((float) 10);//圆角
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);//铺满屏幕
        String url = mPics.get(realPosition);
        Glide.with(context)
                .load(url).asBitmap()
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    public int getDataRealSize(){
        if(mPics != null){
            return mPics.size();
        }
        return  0;
    }
}

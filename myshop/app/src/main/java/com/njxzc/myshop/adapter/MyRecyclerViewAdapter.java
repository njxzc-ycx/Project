package com.njxzc.myshop.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.R;

import java.util.ArrayList;

/**
 * Created by 殷晨旭 on 2021/2/13.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>{

    private final Context context;
    private final ArrayList<String> datas;

    public MyRecyclerViewAdapter(Context context, ArrayList<String> comImageOtherDatas) {
        this.context = context;
        this.datas = comImageOtherDatas;
    }

    /*相当于getView方法中创建View和ViewHolder*/
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(context, R.layout.comimageother_recyclerview_style,null);
        return new MyViewHolder(itemView);
    }

    /*数据和View绑定*/
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //根据位置得到对应的数据
        String data = datas.get(position);
        Glide.with(context)
                .load(data)
                .dontAnimate()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(holder.iv_comImageOther);

    }

    /*得到总条数*/
    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_comImageOther;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_comImageOther = itemView.findViewById(R.id.iv_comImageOther);
        }
    }


}

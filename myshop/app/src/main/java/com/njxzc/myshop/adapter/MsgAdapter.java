package com.njxzc.myshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class MsgAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Map<String,Object>> msgDatas;
    private List<Map<String,Object>> replaysDatas;

    public MsgAdapter(Context context,List<Map<String,Object>> groups){
        this.context=context;
        this.msgDatas=groups;
    }

    @Override
    public int getGroupCount() {
        return msgDatas.size();
    }

    @Override
    public int getChildrenCount(int i) {
        replaysDatas = (List<Map<String, Object>>) msgDatas.get(i).get("replayDatas");
        return replaysDatas.size();
    }

    @Override
    public Object getGroup(int i) {
        return msgDatas.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        replaysDatas = (List<Map<String, Object>>) msgDatas.get(i).get("replayDatas");
        return replaysDatas.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.msg_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView iv_photo = (ImageView)view.findViewById(R.id.iv_photo);
        TextView tv_nickName = (TextView)view.findViewById(R.id.tv_nickName);
        TextView tv_msgDes = (TextView)view.findViewById(R.id.tv_msgDes);
        TextView tv_msgTime = (TextView)view.findViewById(R.id.tv_msgTime);
        TextView tv_ifPublisher = (TextView)view.findViewById(R.id.tv_ifPublisher);
        ImageView iv_sex = (ImageView)view.findViewById(R.id.iv_sex);
        TextView tv_howmany = (TextView)view.findViewById(R.id.tv_howmany);
        //取出每一行需要显示的数据
        Map<String,Object> hm = msgDatas.get(i);
        String photo = (String)hm.get("photo");
        Integer sex = (Integer) hm.get("sex");
        String nickName = (String)hm.get("nickName");
        String msgDes = (String)hm.get("msgDes");
        String msgTime = (String)hm.get("msgTime");
        //把显示的值赋给组件
        if (nickName.equals(ComDetailActivity.publisherName)){
            tv_ifPublisher.setText("卖家");
        }
        tv_howmany.setText((i+1)+"楼");
        tv_nickName.setText(nickName);
        tv_msgDes.setText(msgDes);
        tv_msgTime.setText(msgTime);
        Glide.with(context)
                .load(photo).asBitmap()
                /*.apply(new RequestOptions().circleCrop().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_photo);
        if (sex==1){
            iv_sex.setImageResource(R.drawable.sex_man);
        }else {
            iv_sex.setImageResource(R.drawable.sex_woman);
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        //创建layoutInflater对象
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        //通过样式文件创建view
        View view = layoutInflater.inflate(R.layout.replay_style,null);
        //根据view对象查找样式文件中需要显示的组件
        ImageView iv_photo = (ImageView)view.findViewById(R.id.iv_replayer_photo);
        TextView tv_nickName = (TextView)view.findViewById(R.id.tv_nickName);
        TextView tv_replayDes = (TextView)view.findViewById(R.id.tv_replayDes);
        TextView tv_replayTime = (TextView)view.findViewById(R.id.tv_replayTime);
        TextView tv_msgName = (TextView)view.findViewById(R.id.tv_msgName);
        TextView tv_ifPublisher = (TextView)view.findViewById(R.id.tv_ifPublisher);
        ImageView iv_sex = (ImageView)view.findViewById(R.id.iv_sex);
        //取出每一行需要显示的数据
        replaysDatas = (List<Map<String, Object>>) msgDatas.get(i).get("replayDatas");
        Map<String,Object> hm = replaysDatas.get(i1);
        String msgerName = (String)hm.get("msgerName");
        String photo = (String)hm.get("replayPhoto");
        Integer sex = (Integer) hm.get("replaySex");
        String nickName = (String)hm.get("replayNickName");
        String replayDes = (String)hm.get("replayDes");
        String replayTime = (String)hm.get("replayTime");
        //把显示的值赋给组件
        if (nickName.equals(ComDetailActivity.publisherName)){
            tv_ifPublisher.setText("卖家");
        }
        tv_nickName.setText(nickName);
        tv_replayDes.setText(replayDes);
        tv_replayTime.setText(replayTime);
        tv_msgName.setText("回复@"+msgerName+"：");
        Glide.with(context)
                .load(photo).asBitmap()
                /*.apply(new RequestOptions().circleCrop().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_photo);
        if (sex==1){
            iv_sex.setImageResource(R.drawable.sex_man);
        }else {
            iv_sex.setImageResource(R.drawable.sex_woman);
        }

        return view;
    }



    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}

package com.njxzc.myshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.DiscussDetailActivity;
import com.njxzc.myshop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/2/2.
 */

public class DisMsgAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Map<String,Object>> disMsgDatas;
    private List<Map<String,Object>> disReplayDatas;

    public DisMsgAdapter(Context context, List<Map<String,Object>> groups){
        this.context=context;
        this.disMsgDatas=groups;
    }

    @Override
    public int getGroupCount() {
        return disMsgDatas.size();
    }

    @Override
    public int getChildrenCount(int i) {
        disReplayDatas = (List<Map<String, Object>>) disMsgDatas.get(i).get("disReplayDatas");
        return disReplayDatas.size();
    }

    @Override
    public Object getGroup(int i) {
        return disMsgDatas.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        disReplayDatas = (List<Map<String, Object>>) disMsgDatas.get(i).get("disReplayDatas");
        return disReplayDatas.get(i1);
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
        Map<String,Object> hm = disMsgDatas.get(i);
        String photo = (String)hm.get("photo");
        Integer sex = (Integer) hm.get("sex");
        String nickName = (String)hm.get("nickName");
        String msgDes = (String)hm.get("msgDes");
        String msgTime = (String)hm.get("msgTime");
        int userId = (int) hm.get("userId");
        //把显示的值赋给组件
        if (DiscussDetailActivity.isAnonymity == 1&&userId==DiscussDetailActivity.publisherId){
            tv_nickName.setText("匿名用户");
            if (sex==1){
                iv_photo.setImageResource(R.drawable.anonymity_man);
            }else {
                iv_photo.setImageResource(R.drawable.anonymity_woman);
            }
            tv_ifPublisher.setText("楼主");

        }else {
            if (userId==DiscussDetailActivity.publisherId){
                tv_ifPublisher.setText("楼主");
            }
            tv_nickName.setText(nickName);
            Glide.with(context)
                    .load(photo).asBitmap()
                /*.apply(new RequestOptions().circleCrop().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                    .error(R.drawable.errorpic)
                    .placeholder(R.drawable.defaultpic)
                    .into(iv_photo);
        }

        tv_howmany.setText((i+1)+"楼");

        tv_msgDes.setText(msgDes);
        tv_msgTime.setText(msgTime);

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
        disReplayDatas = (List<Map<String, Object>>) disMsgDatas.get(i).get("disReplayDatas");
        Map<String,Object> hm = disReplayDatas.get(i1);
        String msgerName = (String)hm.get("msgerName");
        String photo = (String)hm.get("replayPhoto");
        Integer sex = (Integer) hm.get("replaySex");
        String nickName = (String)hm.get("replayNickName");
        String replayDes = (String)hm.get("replayDes");
        String replayTime = (String)hm.get("replayTime");
        int userId = (int) hm.get("replayerId");
        int msgerId = (int) hm.get("msgerId");
        //把显示的值赋给组件
        if (DiscussDetailActivity.isAnonymity == 1&&userId==DiscussDetailActivity.publisherId){
            tv_nickName.setText("匿名用户");
            if (sex==1){
                iv_photo.setImageResource(R.drawable.anonymity_man);
            }else {
                iv_photo.setImageResource(R.drawable.anonymity_woman);
            }
            tv_ifPublisher.setText("楼主");
        }else {
            if (userId==DiscussDetailActivity.publisherId){
                tv_ifPublisher.setText("楼主");
            }
            tv_nickName.setText(nickName);
            Glide.with(context)
                    .load(photo).asBitmap()
                /*.apply(new RequestOptions().circleCrop().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                    .error(R.drawable.errorpic)
                    .placeholder(R.drawable.defaultpic)
                    .into(iv_photo);
        }
        if (DiscussDetailActivity.isAnonymity == 1&&msgerId==DiscussDetailActivity.publisherId){
            tv_msgName.setText("回复@匿名用户：");
        }else {
            tv_msgName.setText("回复@" + msgerName + "：");
        }
        tv_replayDes.setText(replayDes);
        tv_replayTime.setText(replayTime);

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

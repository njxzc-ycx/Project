package com.njxzc.myshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.easeui.EaseConstant;
import com.njxzc.myshop.ChatActivity;
import com.njxzc.myshop.ComDetailActivity;
import com.njxzc.myshop.PerHomePageActivity;
import com.njxzc.myshop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 殷晨旭 on 2021/5/3.
 */

public class FollowsAdapter extends BaseAdapter {

    private Context context;

    private List<Map<String,Object>> data;

    public FollowsAdapter(Context context, List<Map<String,Object>> data){
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
        View view = layoutInflater.inflate(R.layout.myfollows_style,null);
        ImageView iv_photo = view.findViewById(R.id.iv_photo);
        ImageView iv_toChat = view.findViewById(R.id.iv_toChat);
        LinearLayout ll_userInfo = view.findViewById(R.id.ll_userInfo);
        ImageView iv_sex = view.findViewById(R.id.iv_sex);
        TextView tv_nickName = view.findViewById(R.id.tv_nickName);
        TextView tv_school = view.findViewById(R.id.tv_school);
        //取出每一行需要显示的数据
        Map<String,Object> hm = data.get(position);
        String photo = (String) hm.get("profilePhoto");
        final String nickName = (String) hm.get("nickName");
        String school = (String) hm.get("school");
        final String userName = (String) hm.get("userName");
        final int followerId = (int) hm.get("followerId");
        int sex = (int) hm.get("sex");
        tv_nickName.setText(nickName);
        tv_school.setText(school);
        if (sex==1){
            iv_sex.setImageResource(R.drawable.sex_man);
        }else {
            iv_sex.setImageResource(R.drawable.sex_woman);
        }
        Glide.with(context)
                .load(photo).asBitmap()
                /*.apply(new RequestOptions().error(R.drawable.errorpic).placeholder(R.drawable.defaultpic))//默认加载图片  加载出错图片*/
                .error(R.drawable.errorpic)
                .placeholder(R.drawable.defaultpic)
                .into(iv_photo);

        ll_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,PerHomePageActivity.class);
                intent.putExtra("userId",followerId);
                context.startActivity(intent);
            }
        });

        iv_toChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(context,ChatActivity.class);
                chat.putExtra("userName",userName);
                chat.putExtra("publisherName",nickName);
                chat.putExtra(EaseConstant.EXTRA_USER_ID,userName);
                chat.putExtra(EaseConstant.EXTRA_USER_NICKNAME,nickName);
                context.startActivity(chat);
            }
        });

        return view;


    }
}

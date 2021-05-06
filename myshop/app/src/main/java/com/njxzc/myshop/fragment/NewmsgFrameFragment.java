package com.njxzc.myshop.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.njxzc.myshop.newmsg_activity.ChatsListActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.base.BaseFragment;
import com.njxzc.myshop.newmsg_activity.NewMsgActivity;

/**
 * Created by 殷晨旭 on 2021/2/4.
 * 消息框架Fragment
 */

public class NewmsgFrameFragment extends BaseFragment {

    private static  final String TAG = NewmsgFrameFragment.class.getSimpleName();//"NewmsgFrameFragment"
    private LinearLayout ll_chats;
    /*private LinearLayout ll_news;*/

    @Override
    protected View initView() {
        Log.e(TAG,"消息框架Fragment页面被初始化了...");
        View view = View.inflate(mContext, R.layout.fragment_news_frame,null);
        ll_chats = view.findViewById(R.id.ll_chats);
        /*ll_news = view.findViewById(R.id.ll_news);*/
        ll_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ChatsListActivity.class);
                startActivity(intent);
            }
        });
        /*ll_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),NewMsgActivity.class);
                startActivity(intent);
            }
        });*/
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}

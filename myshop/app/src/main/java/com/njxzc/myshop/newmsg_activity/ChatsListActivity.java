package com.njxzc.myshop.newmsg_activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.njxzc.myshop.ChatActivity;
import com.njxzc.myshop.R;
import com.njxzc.myshop.personal_activity.UpdatePhoneActivity;
import com.njxzc.myshop.utils.Model;
import com.njxzc.myshop.utils.OkhttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Response;

public class ChatsListActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);
        Model.getInstance().init(this);
        initData();

    }

    private void initData() {
        //创建一个会话的fragment
        EaseConversationListFragment easeConversationListFragment = new EaseConversationListFragment();
        //跳转到会话详情页面
        easeConversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
               final Intent intent =  new Intent(ChatsListActivity.this,ChatActivity.class);

                //传递参数
                intent.putExtra(EaseConstant.EXTRA_USER_ID,conversation.conversationId());
                final String userName = conversation.conversationId();
                        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                            @Override
                            public void run() {
                                String url = OkhttpHelper.getIp()+"/shopsystem/androidUser/findUserByUserName";
                                OkhttpHelper.findUserByUserName(url,userName,new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ChatsListActivity.this, "网络连接失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        final String responseData = response.body().string();
                                        try {
                                            JSONObject user = new JSONObject(responseData);
                                            String nickName = user.getString("nickName");
                                            intent.putExtra(EaseConstant.EXTRA_USER_NICKNAME,nickName);
                                            startActivity(intent);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                });
                            }
                        });




            }
        });

        //监听会话消息
        EMClient.getInstance().chatManager().addMessageListener(emMessageListener);

        //替换fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chatlist,easeConversationListFragment).commit();

    }
    private EMMessageListener emMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //
            EaseUI.getInstance().getNotifier().onNewMesg(messages);
            for (EMMessage message : messages) {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(message.getFrom(), EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
                JSONObject jsonObject = new JSONObject();
                String nickName = message.getStringAttribute("nickName", "");
                String profilePhoto = message.getStringAttribute("profilePhoto", "");
                try {
                    if (!TextUtils.isEmpty(nickName)) {
                        jsonObject.put("nickName", nickName);
                    }
                    if (!TextUtils.isEmpty(profilePhoto)) {
                        jsonObject.put("profilePhoto", profilePhoto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(jsonObject.toString());
                conversation.setExtField(jsonObject.toString());
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageRead(List<EMMessage> list) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }


        public void onMessageRecalled(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };
}

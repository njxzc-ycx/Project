package com.njxzc.myshop;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;
import com.mob.tools.network.MultiPartInputStream;
import com.njxzc.myshop.utils.Constant;

/**
 * Created by 殷晨旭 on 2021/2/26.
 */

public class IMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意之后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意之后才能接受群邀请
        EaseUI.getInstance().init(this,options);
        // 注册连接监听
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int error) {
                if (error==EMError.USER_LOGIN_ANOTHER_DEVICE){
                    onConnectionConflict();
                }

            }
        });

    }
    /**
     * 帐号在别的设备登陆
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Constant.ACCOUNT_CONFLICT=202;
        intent.putExtra("isConflict",Constant.ACCOUNT_CONFLICT);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

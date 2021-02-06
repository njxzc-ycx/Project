package com.njxzc.myshop.pojo;

import android.app.Application;

/**
 * Created by 殷晨旭 on 2021/2/3.
 */

public class AppInfo extends Application {

    private User user;//将对象信息保存

    //无参构造方法
    public AppInfo() {
        super();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}

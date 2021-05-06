package com.njxzc.myshop.utils;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 殷晨旭 on 2021/2/16.
 */
//每次使用线程都new Thread将过多地占用系统资源影响性能，因此采用全局线程池进行统一管理，避免堵塞或内存溢出。
//采用Executors提供的newCachedThreadPool --- 创建一个可缓存线程池，线程长时间不用将被回收，线程池大小取决于JVM。
public class Model {
    private Context mContext;
    private ExecutorService globalThreadPool = Executors.newCachedThreadPool();

    private static Model model = new Model();

    private Model() {

    }

    // 获得单例对象
    public static Model getInstance() {
        return model;
    }

    // 初始化
    public void init(Context context) {
        mContext = context;
    }

    // 获取全局线程池
    public ExecutorService getGlobalThreadPool() {
        return globalThreadPool;
    }
}

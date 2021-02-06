package com.njxzc.myshop.utils;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by 殷晨旭 on 2021/1/27.
 */

public class OkhttpHelper {

    public static void getRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void login(String url, String userName,String password, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userName", userName);
        params.add("userPwd", password);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void codeLogin(String url, String phone, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("phone", phone);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void resetUserPwd(String url, String phone,String userPwd, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("phone", phone);
        params.add("userPwd", userPwd);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateUserPhone(String url, String phone,String userPwd,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("phone", phone);
        params.add("userPwd", userPwd);
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateUserPwd(String url, String oldPwd,String newPwd,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("oldPwd", oldPwd);
        params.add("newPwd", newPwd);
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void register(String url, String userName,String phone,String password, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userName", userName);
        params.add("phone", phone);
        params.add("userPwd", password);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }
}

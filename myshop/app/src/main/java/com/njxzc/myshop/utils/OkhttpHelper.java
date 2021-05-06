package com.njxzc.myshop.utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.provider.CallLog.Calls.TYPE;

/**
 * Created by 殷晨旭 on 2021/1/27.
 */

public class OkhttpHelper {

    public static String getIp(){
        String ip = "http://192.168.43.38:8088";
        return ip;
    }

    public static void getAllCom(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getSameSchoolCom(String url,String school,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("school", school);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findComByComId(String url,String comId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void searchCom(String url,String key,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("key", key);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
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

    public static void findUserByUserName(String url, String userName, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userName", userName);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findUserByUserId(String url, String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
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

    public static void updateIMPWD(String url, String newPwd, Callback callback){
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        try {
            json.put("newpassword", newPwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization","Bearer YWMt8r0iaq5UEeuWiF8sKQ56EgAAAAAAAAAAAAAAAAAAAAEBcErWK9FJGL7UvfAB2grDAgMAAAF5QTMuLQBPGgDTdlKEpQ_yBjzYbyoN5fyIWN4grtz_3BHeLW2wIDtO2w")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateNickName(String url, String nickName,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("nickName", nickName);
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateSex(String url, String sex,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("sex", sex);
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateBirth(String url, String newBirth,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("newBirth", newBirth);
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void insertSchool(String url, String school,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("school", school);
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

    public static void isCollected(String url,String userId,String comId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void cancelCollected(String url,String userId,String comId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void joinCollected(String url,String userId,String comId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addHits(String url,String comId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getCollects(String url,String userId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getComs(String url,String sellerId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("sellerId", sellerId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllTypefirst(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllTypeSecond(String url,String firstId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("firstId", firstId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllTypesecond(String url,String firstId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("firstId", firstId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllTypethird(String url,String secondId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("secondId", secondId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getComByThirdId(String url,String thirdId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("thirdId", thirdId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findMsgByComId(String url,String comId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findMsgAndReplayByComId(String url,String comId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findReplayByComIdAndMsgId(String url,String comId,String msgId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        params.add("msgId", msgId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void addMessage(String url,String userId,String comId,String msgTime,String msgDes,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        params.add("userId", userId);
        params.add("msgTime", msgTime);
        params.add("msgDes", msgDes);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void addReplay(String url,String msgId,String msgerId,String replayerId,String replayDes,String replayTime,String comId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        params.add("msgId", msgId);
        params.add("msgerId", msgerId);
        params.add("replayerId", replayerId);
        params.add("replayDes", replayDes);
        params.add("replayTime", replayTime);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void findAllAddressByUserId(String url,String userId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void findAllAddressCount(String url,String userId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void findAddressByAddressId(String url,String addressId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("addressId", addressId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void addAddress(String url,String userId,String address,String area,String phone,String linkman,String defaultAddress,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("address", address);
        params.add("area", area);
        params.add("phone", phone);
        params.add("linkman", linkman);
        params.add("defaultAddress", defaultAddress);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void updateAddress(String url,String userId,String addressId,String address,String area,String phone,String linkman,String defaultAddress,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("addressId", addressId);
        params.add("address", address);
        params.add("area", area);
        params.add("phone", phone);
        params.add("linkman", linkman);
        params.add("defaultAddress", defaultAddress);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void withdrawCom(String url,String comId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void deleteDis(String url,String discussId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void buyCom(String url,String comId,String userId,String addressId,String orderTime,
            String total,String buyCount,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        params.add("userId", userId);
        params.add("addressId", addressId);
        params.add("orderTime", orderTime);
        params.add("buyCount", buyCount);
        params.add("total", total);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllDiscuss(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getRequireDiscuss(String url, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("typeId", "1");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getInteractDiscuss(String url, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("typeId", "2");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getAllDiscussType(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void addDiscussUp(String url,String discussId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addDisscussHits(String url,String discussId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findDiscussByDiscussId(String url,String discussId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findDisMsgAndDisReplayBydiscussId(String url,String discussId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void addDisReplay(String url,String msgId,String msgerId,String replayerId,String replayDes,String replayTime,String discussId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        params.add("msgId", msgId);
        params.add("msgerId", msgerId);
        params.add("replayerId", replayerId);
        params.add("replayDes", replayDes);
        params.add("replayTime", replayTime);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void addDisMessage(String url,String userId,String discussId,String msgTime,String msgDes,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("discussId", discussId);
        params.add("userId", userId);
        params.add("msgTime", msgTime);
        params.add("msgDes", msgDes);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void alipay(String url,String orderNo,String amount,String body,String comId,String count,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("orderNo", orderNo);
        params.add("amount", amount);
        params.add("body", body);
        params.add("comId", comId);
        params.add("count", count);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void findSellerOrder(String url,String sellerId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("sellerId", sellerId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findBuyerOrder(String url,String userId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sellerManageOrder(String url,String orderId,String status,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("orderId", orderId);
        params.add("status", status);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getOrder(String url,String orderId,String status,String getTime,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("orderId", orderId);
        params.add("status", status);
        params.add("getTime", getTime);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void updateCommodityInfoAndUserOrderInfoByComId(String url,String comId,String count,String orderId,String status,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("comId", comId);
        params.add("count", count);
        params.add("orderId", orderId);
        params.add("status", status);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();

        client.newCall(request).enqueue(callback);
    }

    public static void findSlideshows(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void getDiscussByUserId(String url,String userId,Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void isFollowed(String url,String followerId ,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("followerId", followerId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void Followed(String url,String followerId ,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        params.add("followerId", followerId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void findFollowByUserId(String url,String userId, Callback callback){
        FormBody.Builder params = new FormBody.Builder();
        params.add("userId", userId);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(params.build())
                .build();
        client.newCall(request).enqueue(callback);
    }



}

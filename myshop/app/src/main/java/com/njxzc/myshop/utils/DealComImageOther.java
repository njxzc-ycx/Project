package com.njxzc.myshop.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 殷晨旭 on 2021/2/13.
 */

public class DealComImageOther {

    public static ArrayList<String> dealComImageOther(String comImageOther) {
        if(null==comImageOther){
            return null;
        }else {
            String imageUrls[] = comImageOther.split(" ");//将String字符串解析为数组
            ArrayList<String> urlList = new ArrayList<>();
            for (String url : imageUrls) {
                urlList.add(url);
            }
            return urlList;
        }
    }
}

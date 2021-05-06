package com.njxzc.myshop.utils;

import java.util.ArrayList;

/**
 * Created by 殷晨旭 on 2021/2/22.
 */

public class DealSchoolsJson {

    public static ArrayList<String> dealSchoolsJson(String schools) {
        if(null==schools){
            return null;
        }else {
            String schoolsName[] = schools.split(",");//将String字符串解析为数组
            ArrayList<String> schoolList = new ArrayList<>();
            for (String name : schoolsName) {
                schoolList.add(name);
            }
            return schoolList;
        }
    }
}

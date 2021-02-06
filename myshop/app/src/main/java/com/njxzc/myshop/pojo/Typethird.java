package com.njxzc.myshop.pojo;

/**
 * Created by 殷晨旭 on 2021/1/27.
 */

public class Typethird {
    private Integer thirdId;
    private String thirdName;
    private String thirdPicture;

    public Typethird() {
    }

    public Typethird(Integer thirdId, String thirdName, String thirdPicture) {
        this.thirdId = thirdId;
        this.thirdName = thirdName;
        this.thirdPicture = thirdPicture;
    }

    public Integer getThirdId() {
        return thirdId;
    }

    public void setThirdId(Integer thirdId) {
        this.thirdId = thirdId;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getThirdPicture() {
        return thirdPicture;
    }

    public void setThirdPicture(String thirdPicture) {
        this.thirdPicture = thirdPicture;
    }

    @Override
    public String toString() {
        return "Typethird{" +
                "thirdId=" + thirdId +
                ", thirdName='" + thirdName + '\'' +
                ", thirdPicture='" + thirdPicture + '\'' +
                '}';
    }
}

package com.njxzc.myshop.pojo;

import java.sql.Timestamp;

/**
 * Created by 殷晨旭 on 2021/1/27.
 */

public class Commodity {
    private Integer comId;       //COM_ID
    private User user;           //SELLER_ID
    private String comName;      //COM_NAME
    private Double primePrice;   //PRIME_PRICE 原价
    private Double currentPrice; //CURRENT_PRICE 现价
    private String des;         //DES
    private Integer flag;        //FLAG
    private Integer count;       //COUNT
    private Typethird typethird;
    private Integer typeThirdId;
    private Integer isNew;       //IS_NEW
    private Integer isUrgent;    //IS_URGENT
    private Integer isRecommend; //IS_RECOMMEND
    private Integer hits;        //HITS
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp inTime;    //IN_TIME
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp onTime;    //ON_TIME
    private Integer reviews;     //REVIEWS
    private Integer collects;    //COLLECTS
    private String comImageMain;  //COM_IMAGE_MAIN
    private String comImageOther; //COM_IMAGE_OTHER

    public Commodity() {
    }

    public Commodity(Integer comId, User user, String comName, Double primePrice, Double currentPrice, String des, Integer flag, Integer count, Typethird typethird, Integer typeThirdId, Integer isNew, Integer isRecommend, Integer isUrgent, Integer hits, Timestamp inTime, Timestamp onTime, Integer reviews, Integer collects, String comImageMain, String comImageOther) {
        this.comId = comId;
        this.user = user;
        this.comName = comName;
        this.primePrice = primePrice;
        this.currentPrice = currentPrice;
        this.des = des;
        this.flag = flag;
        this.count = count;
        this.typethird = typethird;
        this.typeThirdId = typeThirdId;
        this.isNew = isNew;
        this.isRecommend = isRecommend;
        this.isUrgent = isUrgent;
        this.hits = hits;
        this.inTime = inTime;
        this.onTime = onTime;
        this.reviews = reviews;
        this.collects = collects;
        this.comImageMain = comImageMain;
        this.comImageOther = comImageOther;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public Double getPrimePrice() {
        return primePrice;
    }

    public void setPrimePrice(Double primePrice) {
        this.primePrice = primePrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Typethird getTypethird() {
        return typethird;
    }

    public void setTypethird(Typethird typethird) {
        this.typethird = typethird;
    }

    public Integer getTypeThirdId() {
        return typeThirdId;
    }

    public void setTypeThirdId(Integer typeThirdId) {
        this.typeThirdId = typeThirdId;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsUrgent() {
        return isUrgent;
    }

    public void setIsUrgent(Integer isUrgent) {
        this.isUrgent = isUrgent;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Timestamp getInTime() {
        return inTime;
    }

    public void setInTime(Timestamp inTime) {
        this.inTime = inTime;
    }

    public Timestamp getOnTime() {
        return onTime;
    }

    public void setOnTime(Timestamp onTime) {
        this.onTime = onTime;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public Integer getCollects() {
        return collects;
    }

    public void setCollects(Integer collects) {
        this.collects = collects;
    }

    public String getComImageMain() {
        return comImageMain;
    }

    public void setComImageMain(String comImageMain) {
        this.comImageMain = comImageMain;
    }

    public String getComImageOther() {
        return comImageOther;
    }

    public void setComImageOther(String comImageOther) {
        this.comImageOther = comImageOther;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "comId=" + comId +
                ", user=" + user +
                ", comName='" + comName + '\'' +
                ", primePrice=" + primePrice +
                ", currentPrice=" + currentPrice +
                ", des='" + des + '\'' +
                ", flag=" + flag +
                ", count=" + count +
                ", typethird=" + typethird +
                ", typeThirdId=" + typeThirdId +
                ", isNew=" + isNew +
                ", isUrgent=" + isUrgent +
                ", isRecommend=" + isRecommend +
                ", hits=" + hits +
                ", inTime=" + inTime +
                ", onTime=" + onTime +
                ", reviews=" + reviews +
                ", collects=" + collects +
                ", comImageMain='" + comImageMain + '\'' +
                ", comImageOther='" + comImageOther + '\'' +
                '}';
    }
}

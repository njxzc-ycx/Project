package com.njxzc.shopsystem.pojo;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;


public class Commodity implements Serializable {
    private Integer comId;       //COM_ID
    private String comName;      //COM_NAME
    private Integer sellerId;
    private Double primePrice;   //PRIME_PRICE 原价
    private Double currentPrice; //CURRENT_PRICE 现价
    private String des;         //DES
    private Integer flag;        //FLAG
    private Integer count;       //COUNT
    private Integer typesId;
    private Integer isNew;       //IS_NEW
    private Integer isBargain;       //IS_NEW
    private Integer isUrgent;    //IS_URGENT
    private Integer isRecommend; //IS_RECOMMEND
    private Integer hits;        //HITS
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp inTime;    //IN_TIME
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp onTime;    //ON_TIME
    private Integer reviews;     //REVIEWS
    private Integer collects;    //COLLECTS
    private String comImageMain;  //COM_IMAGE_MAIN
    private String comImageOther; //COM_IMAGE_OTHER
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;    //ON_TIME

    private User user;
    private Typethird typethird;//一对一映射 一个商品只可能属于一个种类


    public Commodity() {
    }

    public Commodity(Integer comId, String comName, Integer sellerId, Double primePrice, Double currentPrice, String des, Integer flag, Integer count, Integer typesId, Integer isNew, Integer isBargain, Integer isUrgent, Integer isRecommend, Integer hits, Timestamp inTime, Timestamp onTime, Integer reviews, Integer collects, String comImageMain, String comImageOther, Timestamp updateTime, User user, Typethird typethird) {
        this.comId = comId;
        this.comName = comName;
        this.sellerId = sellerId;
        this.primePrice = primePrice;
        this.currentPrice = currentPrice;
        this.des = des;
        this.flag = flag;
        this.count = count;
        this.typesId = typesId;
        this.isNew = isNew;
        this.isBargain = isBargain;
        this.isUrgent = isUrgent;
        this.isRecommend = isRecommend;
        this.hits = hits;
        this.inTime = inTime;
        this.onTime = onTime;
        this.reviews = reviews;
        this.collects = collects;
        this.comImageMain = comImageMain;
        this.comImageOther = comImageOther;
        this.updateTime = updateTime;
        this.user = user;
        this.typethird = typethird;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
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

    public Integer getTypesId() {
        return typesId;
    }

    public void setTypesId(Integer typesId) {
        this.typesId = typesId;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getIsBargain() {
        return isBargain;
    }

    public void setIsBargain(Integer isBargain) {
        this.isBargain = isBargain;
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

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Typethird getTypethird() {
        return typethird;
    }

    public void setTypethird(Typethird typethird) {
        this.typethird = typethird;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "comId=" + comId +
                ", comName='" + comName + '\'' +
                ", sellerId=" + sellerId +
                ", primePrice=" + primePrice +
                ", currentPrice=" + currentPrice +
                ", des='" + des + '\'' +
                ", flag=" + flag +
                ", count=" + count +
                ", typesId=" + typesId +
                ", isNew=" + isNew +
                ", isBargain=" + isBargain +
                ", isUrgent=" + isUrgent +
                ", isRecommend=" + isRecommend +
                ", hits=" + hits +
                ", inTime=" + inTime +
                ", onTime=" + onTime +
                ", reviews=" + reviews +
                ", collects=" + collects +
                ", comImageMain='" + comImageMain + '\'' +
                ", comImageOther='" + comImageOther + '\'' +
                ", updateTime=" + updateTime +
                ", user=" + user +
                ", typethird=" + typethird +
                '}';
    }
}

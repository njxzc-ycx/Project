package com.njxzc.shopsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Discuss {
    private Integer discussId;
    private String discussDes;
    private String discussImages;
    private Integer discussUp;
    private Integer discussComments;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp discussTime;
    private Integer discussHits;
    private Integer userId;
    private Integer typeId;
    private Integer isAnonymity;
    private Integer flag;

    private User user;
    private DiscussType discussType;

    public Discuss() {
    }

    public Discuss(Integer discussId, String discussDes, String discussImages, Integer discussUp, Integer discussComments, Timestamp discussTime, Integer discussHits, Integer userId, Integer typeId, Integer isAnonymity, Integer flag, User user, DiscussType discussType) {
        this.discussId = discussId;
        this.discussDes = discussDes;
        this.discussImages = discussImages;
        this.discussUp = discussUp;
        this.discussComments = discussComments;
        this.discussTime = discussTime;
        this.discussHits = discussHits;
        this.userId = userId;
        this.typeId = typeId;
        this.isAnonymity = isAnonymity;
        this.flag = flag;
        this.user = user;
        this.discussType = discussType;
    }

    public Integer getDiscussId() {
        return discussId;
    }

    public void setDiscussId(Integer discussId) {
        this.discussId = discussId;
    }

    public String getDiscussDes() {
        return discussDes;
    }

    public void setDiscussDes(String discussDes) {
        this.discussDes = discussDes;
    }

    public String getDiscussImages() {
        return discussImages;
    }

    public void setDiscussImages(String discussImages) {
        this.discussImages = discussImages;
    }

    public Integer getDiscussUp() {
        return discussUp;
    }

    public void setDiscussUp(Integer discussUp) {
        this.discussUp = discussUp;
    }

    public Integer getDiscussComments() {
        return discussComments;
    }

    public void setDiscussComments(Integer discussComments) {
        this.discussComments = discussComments;
    }

    public Timestamp getDiscussTime() {
        return discussTime;
    }

    public void setDiscussTime(Timestamp discussTime) {
        this.discussTime = discussTime;
    }

    public Integer getDiscussHits() {
        return discussHits;
    }

    public void setDiscussHits(Integer discussHits) {
        this.discussHits = discussHits;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getIsAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(Integer isAnonymity) {
        this.isAnonymity = isAnonymity;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DiscussType getDiscussType() {
        return discussType;
    }

    public void setDiscussType(DiscussType discussType) {
        this.discussType = discussType;
    }

    @Override
    public String toString() {
        return "Discuss{" +
                "discussId=" + discussId +
                ", discussDes='" + discussDes + '\'' +
                ", discussImages='" + discussImages + '\'' +
                ", discussUp=" + discussUp +
                ", discussComments=" + discussComments +
                ", discussTime=" + discussTime +
                ", discussHits=" + discussHits +
                ", userId=" + userId +
                ", typeId=" + typeId +
                ", isAnonymity=" + isAnonymity +
                ", flag=" + flag +
                ", user=" + user +
                ", discussType=" + discussType +
                '}';
    }
}

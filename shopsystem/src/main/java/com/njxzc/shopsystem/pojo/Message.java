package com.njxzc.shopsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class Message {
    private Integer msgId;
    private String msgDes;
    private Integer comId;
    private Integer userId;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp msgTime;
    private Integer msgUp;
    private User user;

    public Message() {
    }

    public Message(Integer msgId, String msgDes, Integer comId, Integer userId, Timestamp msgTime, Integer msgUp, User user) {
        this.msgId = msgId;
        this.msgDes = msgDes;
        this.comId = comId;
        this.userId = userId;
        this.msgTime = msgTime;
        this.msgUp = msgUp;
        this.user = user;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getMsgDes() {
        return msgDes;
    }

    public void setMsgDes(String msgDes) {
        this.msgDes = msgDes;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Timestamp msgTime) {
        this.msgTime = msgTime;
    }

    public Integer getMsgUp() {
        return msgUp;
    }

    public void setMsgUp(Integer msgUp) {
        this.msgUp = msgUp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msgId=" + msgId +
                ", msgDes='" + msgDes + '\'' +
                ", comId=" + comId +
                ", userId=" + userId +
                ", msgTime=" + msgTime +
                ", msgUp=" + msgUp +
                ", user=" + user +
                '}';
    }
}

package com.njxzc.shopsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Time;
import java.sql.Timestamp;

public class Replay {
    private Integer replayId;
    private String replayDes;
    private Integer replayerId;
    private Integer msgId;
    private Integer msgerId;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp replayTime;
    private User user;

    public Replay() {
    }

    public Replay(Integer replayId, String replayDes, Integer replayerId, Integer msgId, Integer msgerId, Timestamp replayTime, User user) {
        this.replayId = replayId;
        this.replayDes = replayDes;
        this.replayerId = replayerId;
        this.msgId = msgId;
        this.msgerId = msgerId;
        this.replayTime = replayTime;
        this.user = user;
    }

    public Integer getReplayId() {
        return replayId;
    }

    public void setReplayId(Integer replayId) {
        this.replayId = replayId;
    }

    public String getReplayDes() {
        return replayDes;
    }

    public void setReplayDes(String replayDes) {
        this.replayDes = replayDes;
    }

    public Integer getReplayerId() {
        return replayerId;
    }

    public void setReplayerId(Integer replayerId) {
        this.replayerId = replayerId;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public Integer getMsgerId() {
        return msgerId;
    }

    public void setMsgerId(Integer msgerId) {
        this.msgerId = msgerId;
    }

    public Timestamp getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(Timestamp replayTime) {
        this.replayTime = replayTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Replay{" +
                "replayId=" + replayId +
                ", replayDes='" + replayDes + '\'' +
                ", replayerId=" + replayerId +
                ", msgId=" + msgId +
                ", msgerId=" + msgerId +
                ", replayTime=" + replayTime +
                ", user=" + user +
                '}';
    }
}

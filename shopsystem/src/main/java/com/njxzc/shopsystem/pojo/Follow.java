package com.njxzc.shopsystem.pojo;

public class Follow {
    private Integer followId;
    private Integer followerId;
    private Integer userId;
    private User user;

    public Follow() {
    }

    public Follow(Integer followId, Integer followerId, Integer userId, User user) {
        this.followId = followId;
        this.followerId = followerId;
        this.userId = userId;
        this.user = user;
    }

    public Integer getFollowId() {
        return followId;
    }

    public void setFollowId(Integer followId) {
        this.followId = followId;
    }

    public Integer getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Integer followerId) {
        this.followerId = followerId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "followId=" + followId +
                ", followerId=" + followerId +
                ", userId=" + userId +
                ", user=" + user +
                '}';
    }
}

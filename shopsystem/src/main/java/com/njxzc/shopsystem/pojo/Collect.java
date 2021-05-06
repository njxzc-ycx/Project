package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class Collect implements Serializable {

    private Integer collectId;
    private Integer comId;
    private Integer userId;

    private User user;
    private Commodity commodity;
    private Typethird typethird;

    public Typethird getTypethird() {
        return typethird;
    }

    public void setTypethird(Typethird typethird) {
        this.typethird = typethird;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collect() {
    }

    public Collect(Integer collectId, Integer comId, Integer userId) {
        this.collectId = collectId;
        this.comId = comId;
        this.userId = userId;
    }

    public Integer getCollectId() {
        return collectId;
    }

    public void setCollectId(Integer collectId) {
        this.collectId = collectId;
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

    @Override
    public String toString() {
        return "Collect{" +
                "collectId=" + collectId +
                ", comId=" + comId +
                ", userId=" + userId +
                '}';
    }
}

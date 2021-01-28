package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class OrderDetail implements Serializable {

    private int orderDetail;
    private int orderId;
    private int comId;
    private int buyCount;

    public OrderDetail() {
    }

    public OrderDetail(int orderDetail, int orderId, int comId, int buyCount) {
        this.orderDetail = orderDetail;
        this.orderId = orderId;
        this.comId = comId;
        this.buyCount = buyCount;
    }

    public int getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(int orderDetail) {
        this.orderDetail = orderDetail;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetail=" + orderDetail +
                ", orderId=" + orderId +
                ", comId=" + comId +
                ", buyCount=" + buyCount +
                '}';
    }
}

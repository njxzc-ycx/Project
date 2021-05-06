package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class OrderDetail implements Serializable {

    private int orderDetailId;
    private int orderId;
    private int comId;
    private int buyCount;

    private Commodity commodity;//一对一关系

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, int comId, int buyCount) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.comId = comId;
        this.buyCount = buyCount;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
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
                "orderDetailId=" + orderDetailId +
                ", orderId=" + orderId +
                ", comId=" + comId +
                ", buyCount=" + buyCount +
                '}';
    }
}

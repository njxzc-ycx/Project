package com.njxzc.shopsystem.pojo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserOrder implements Serializable {

    private int orderId;
    private int userId;
    private int addressId;
    private Timestamp orderTime;
    private Timestamp getTime;
    private double total;
    private String note;

    private Address address;//一个订单只可能有一个地址 一对一
    private List<OrderDetail> orderDetailList;


    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public UserOrder() {
    }

    public UserOrder(int orderId, int userId, int addressId, Timestamp orderTime, Timestamp getTime, double total, String note) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.orderTime = orderTime;
        this.getTime = getTime;
        this.total = total;
        this.note = note;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Timestamp getGetTime() {
        return getTime;
    }

    public void setGetTime(Timestamp getTime) {
        this.getTime = getTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", addressId=" + addressId +
                ", orderTime=" + orderTime +
                ", getTime=" + getTime +
                ", total=" + total +
                ", note='" + note + '\'' +
                '}';
    }
}

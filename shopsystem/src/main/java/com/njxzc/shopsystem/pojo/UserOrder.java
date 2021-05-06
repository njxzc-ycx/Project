package com.njxzc.shopsystem.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class UserOrder implements Serializable {

    private int orderId;
    private int userId;
    private int addressId;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp orderTime;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp getTime;
    private double total;
    private String note;
    private int status;//0为买家未付款 1为买家已付款 未发货 2为买家已发货 3为已收货 4为取消订单

    private Address address;//一个订单只可能有一个地址 一对一
    private List<OrderDetail> orderDetailList;
    private OrderDetail orderDetail;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public UserOrder() {
    }

    public UserOrder(int orderId, int userId, int addressId, Timestamp orderTime, Timestamp getTime, double total, String note, int status) {
        this.orderId = orderId;
        this.userId = userId;
        this.addressId = addressId;
        this.orderTime = orderTime;
        this.getTime = getTime;
        this.total = total;
        this.note = note;
        this.status = status;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
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
                ", status=" + status +
                ", address=" + address +
                ", orderDetailList=" + orderDetailList +
                '}';
    }
}

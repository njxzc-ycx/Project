package com.njxzc.shopsystem.pojo;

import java.io.Serializable;

public class Address implements Serializable {

    private int addressId;
    private int userId;
    private String address;
    private String area;
    private String phone;
    private String linkman;
    private int defaultAddress;
    private String addressNote;

    public Address() {
    }

    public Address(int addressId, int userId, String address, String area, String phone, String linkman, int defaultAddress, String addressNote) {
        this.addressId = addressId;
        this.userId = userId;
        this.address = address;
        this.area = area;
        this.phone = phone;
        this.linkman = linkman;
        this.defaultAddress = defaultAddress;
        this.addressNote = addressNote;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public int getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getAddressNote() {
        return addressNote;
    }

    public void setAddressNote(String addressNote) {
        this.addressNote = addressNote;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", userId=" + userId +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", phone='" + phone + '\'' +
                ", linkman='" + linkman + '\'' +
                ", defaultAddress=" + defaultAddress +
                ", addressNote='" + addressNote + '\'' +
                '}';
    }
}

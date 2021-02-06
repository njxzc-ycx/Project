package com.njxzc.shopsystem.pojo;

import java.util.Date;
import java.util.List;

public class User {
    private Integer userId;
    private String userName;
    private String userPwd;
    private String nickName;
    private String realName;
    private String grade;
    private Integer sex;
    private Date birth;
    private Integer state;
    private String school;
    private String email;
    private String phone;
    private String profilePhoto;
    private double balance;

    private List<UserOrder> userOrderList;//一个用户可以用多个订单

    public List<UserOrder> getUserOrderList() {
        return userOrderList;
    }

    public void setUserOrderList(List<UserOrder> userOrderList) {
        this.userOrderList = userOrderList;
    }

    public User() {
    }

    public User(Integer userId, String userName, String userPwd, String nickName, String realName, String grade, Integer sex, Date birth, Integer state, String school, String email, String phone, String profilePhoto, double balance) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.nickName = nickName;
        this.realName = realName;
        this.grade = grade;
        this.sex = sex;
        this.birth = birth;
        this.state = state;
        this.school = school;
        this.email = email;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.balance = balance;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", grade='" + grade + '\'' +
                ", sex=" + sex +
                ", birth=" + birth +
                ", state=" + state +
                ", school='" + school + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", balance=" + balance +
                '}';
    }
}

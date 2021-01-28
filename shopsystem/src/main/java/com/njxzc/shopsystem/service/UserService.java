package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.User;

import java.util.List;

public interface UserService {

    //查询所有用户业务逻辑 查询后，返回所有用户信息
    List<User> findAllUsers();

    //测试xml中
    List<User> findAllUsers2();

    //登录功能业务逻辑 考虑到登录情况比较多
    String login(User user);

    //根据用户名查询用户
    User findUserByUserName(User user);

    //用户注册业务逻辑
    boolean registerUser(User user);
    //用户信息修改业务逻辑

    //查询用户是否存在业务逻辑
    boolean findUserByUserNameExist(String userName);



}

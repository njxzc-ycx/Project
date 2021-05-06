package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {

    //查询所有用户业务逻辑 查询后，返回所有用户信息
    List<User> findAllUsers();

    List<User> findAllUsers2();

    //根据关键字查询商品
    List<User> findUsersByKey(String key,int userId,String school,int state);

    //登录功能业务逻辑 考虑到登录情况比较多
    String login(User user);

    User login2(User user);

    //根据用户名查询用户
    User findUserByUserName(User user);

    //根据用户编号查询用户
    User findUserByUserId(int userId);

    //用户注册业务逻辑
    boolean registerUser(User user);
    //用户信息修改业务逻辑

    //查询用户是否存在业务逻辑
    boolean findUserByUserNameExist(String userName);

    //购买商品更新用户余额业务逻辑
    boolean updateBalanceByUserId(double balance,int userId);

    //查询号码验证登录
    boolean findUserByPhoneExist(String phone);

    User findUserByPhone(String phone);

    //通过手机验证修改/找回密码
    boolean updatePwdByPhone(User user);

    //通过用户编号修改手机号和用户名
    boolean updatePhoneAndUserNameByUserId(User user);

    //根据用户编号密码 查询原密码是否正确
    boolean checkOldPwdByUserId(User user);

    //根据用户编号修改密码
    boolean updateUserPwdByUserId(User user);

    //根据用户昵称 查询昵称是否已经被占用
    boolean checkOnlyNickName(User user);

    //更新用户昵称
    boolean updateNickNameByUserId(User user);

    //更新用户性别
    boolean updateSexByUserId(User user);

    //更新用户生日
    boolean updateBirthByUserId(User user);

    //更新头像生日
    boolean updatePhotoByUserId(User user);

    //更新用户学校
    boolean insertSchoolByUserId(User user);

    //查询用户数
    int findUserCount();

    //改变用户状态
    boolean updateUserState(User user);







}

package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("SELECT USER_ID userId,USER_NAME userName,USER_PWD userPwd,REAL_NAME realName," +
            "grade,sex,birth,state,school,email,phone,PROFILE_PHOTO profilePhoto,balance FROM user")
    List<User> findAllUsers();

    //使用xml编写sql语句
    List<User> findAllUsers2();

    //根据用户名查询用户 如果传递多个值请使用对象
    User findUserByUserName(User user);

    //根据用户名查询用户 如果传递多个值请使用对象
    User findUserByUserNameAndPwd(User user);

    //插入用户
    int registerUser(User user);

    //账号查询用户是否存在
    int findUserByUserNameExist(@Param("userName") String userName);

    //查询号码验证登录
    int findUserByPhoneExist(@Param("phone") String phone);

    User findUserByPhone(@Param("phone") String phone);

    //购买后更新用户余额
    int updateBalanceByUserId(@Param("balance") double balance,@Param("userId") int userId);

    //通过手机验证修改/找回密码
    int updatePwdByPhone(User user);

    //通过用户编号修改手机号和用户名
    int updatePhoneAndUserNameByUserId(User user);

    //根据用户编号密码 查询原密码是否正确
    int checkOldPwdByUserId(User user);

    //根据用户编号修改密码
    int updateUserPwdByUserId(User user);



}

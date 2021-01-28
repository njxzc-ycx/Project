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

    //插入用户
    int registerUser(User user);

    //账号查询用户是否存在
    int findUserByUserNameExist(@Param("userName") String userName);


}

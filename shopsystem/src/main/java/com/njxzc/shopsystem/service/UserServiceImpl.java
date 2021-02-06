package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.UserMapper;
import com.njxzc.shopsystem.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("userService")//定义UserServiceImpl是一个Service命名为 userService
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;//通过注解 载入UserMapper

    @Override
    public List<User> findAllUsers() {
        System.out.println("执行了查询所有用户的业务逻辑");//sout 打印快捷键
        return userMapper.findAllUsers();//调用UserMapper中查询所有用户的方法
    }

    @Override
    public List<User> findAllUsers2() {
        return userMapper.findAllUsers2();//调用UserMapper中查询所有用户的方法
    }

    @Override
    public String login(User user) {
        //1.根据用户名查询用户
        User getUser = userMapper.findUserByUserName(user);
        if(null==getUser){
            return "nameErr";
        }else {
            if (getUser.getUserPwd().equals(user.getUserPwd())){
                //还需要区分是否状态可用
                if(0==getUser.getState()) {
                    return "locked";
                }else {
                    return "success";
                }
            }else {
                return "pwdErr";
            }
        }
    }

    @Override
    public User login2(User user) {
        //1.根据用户名查询用户
        User getUser = userMapper.findUserByUserNameAndPwd(user);
        return  getUser;
    }

    @Override
    public User findUserByUserName(User user) {
       System.out.println("根据用户名查询用户");
        return userMapper.findUserByUserName(user);
    }

    @Override
    public boolean registerUser(User user) {
        try {
            //注册成功 result会返回大于0的数值
            int result = userMapper.registerUser(user);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public boolean findUserByUserNameExist(String userName) {
        try {
            int result = userMapper.findUserByUserNameExist(userName);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateBalanceByUserId(double balance,int userId) {
        try {
            int result = userMapper.updateBalanceByUserId(balance,userId);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean findUserByPhoneExist(String phone) {
        try {
            int result = userMapper.findUserByPhoneExist(phone);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public User findUserByPhone(String phone) {
        return userMapper.findUserByPhone(phone);
    }

    @Override
    public boolean updatePwdByPhone(User user) {
        try {
            int result = userMapper.updatePwdByPhone(user);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updatePhoneAndUserNameByUserId(User user) {
        try {
            int result = userMapper.updatePhoneAndUserNameByUserId(user);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean checkOldPwdByUserId(User user) {
        try {
            int result = userMapper.checkOldPwdByUserId(user);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateUserPwdByUserId(User user) {
        try {
            int result = userMapper.updateUserPwdByUserId(user);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}

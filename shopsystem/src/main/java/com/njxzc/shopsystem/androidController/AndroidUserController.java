package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserService;
import com.njxzc.shopsystem.utils.randomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("androidUser")
public class AndroidUserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public String login(User user)throws Exception {
        String result = userService.login(user);
        String msg = "";
        switch (result) {
            case "nameErr":
                msg = "nameErr";
                break;
            case "pwdErr":
                msg = "pwdErr";
                break;
            case "locked":
                msg = "locked";
                break;
            case "success":
                msg = "success";
                break;

        }
        return msg;
    }

    @PostMapping("/login2")
    public User login2(User user)throws Exception {
        return userService.login2(user);
    }

    @PostMapping("/registerUser")
    @ResponseBody
    public String registerUser(User user){
        String userName = user.getUserName();
        boolean check = userService.findUserByUserNameExist(userName);
        System.out.println(check);
        if(check){
            return "userNameExist";
        }else {
            String profilePhotoUrl = "http://192.168.0.103:8088/shopsystem/resources/image/default.jpg"; //默认头像地址
            user.setProfilePhoto(profilePhotoUrl);
            String nickName = randomUtil.getRandomString(12);//随机生成昵称
            user.setNickName(nickName);
            boolean result = userService.registerUser(user);
            if (result) {
                return "registerSuccess";
            } else {
                return "registerFail";
            }
        }
    }

    @PostMapping("/findUserPhone")
    public User findUserPhone(User user)throws Exception {
        String phone = user.getPhone();
        return userService.findUserByPhone(phone);
    }

    @PostMapping("/resetUserPwd")
    public String resetUserPwd(User user)throws Exception {
        String phone = user.getPhone();
        boolean check = userService.findUserByPhoneExist(phone);
        if(check){
            boolean result = userService.updatePwdByPhone(user);
            if(result){
                return "resetPwdSuccess";
            }else {
                return "resetPwdFail";
            }
        }else {
            return "phoneNoExist";
        }
    }

    @PostMapping("/updateUserPhone")
    public String updateUserPhone(User user)throws Exception {
        String phone = user.getPhone();
        boolean checkOldPwd = userService.checkOldPwdByUserId(user);
        if(checkOldPwd){
            boolean checkPhoneExist = userService.findUserByPhoneExist(phone);
            if(checkPhoneExist){
                return "phoneExist";
            }else {
                boolean result = userService.updatePhoneAndUserNameByUserId(user);
                if(result){
                    return "updatePhoneSuccess";
                }else {
                    return "updatePhoneFail";
                }
            }
        }else {
            return "oldPwdError";
        }
    }

    @PostMapping("/updateUserPwd")
    public String updateUserPwd(String oldPwd,String newPwd,int userId)throws Exception {
        User user = new User();
        user.setUserPwd(oldPwd);
        user.setUserId(userId);
        boolean checkOldPwd = userService.checkOldPwdByUserId(user);
        if(checkOldPwd){
            user.setUserPwd(newPwd);
            boolean result = userService.updateUserPwdByUserId(user);
            if(result){
                return "updateUserPwdSuccess";
            }else {
                return "updateUserPwdFail";
            }

        }else {
            return "oldPwdError";
        }
    }



}

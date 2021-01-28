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

    @PostMapping("/registerUser")
    @ResponseBody
    public String registerUser(User user){
        String userName = user.getUserName();
        boolean check = userService.findUserByUserNameExist(userName);
        System.out.println(check);
        if(check){
            return "userNameExist";
        }else {
            String profilePhotoUrl = "http://localhost:8088/shopsystem/resources/image/default.jpg"; //默认头像地址
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



}

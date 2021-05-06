package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserService;
import com.njxzc.shopsystem.utils.randomUtil;
import com.njxzc.shopsystem.utils.uploadUtil;
import org.apache.http.HttpRequest;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            String profilePhotoUrl = "http://192.168.43.38:8088/shopsystem/resources/image/default.jpg"; //默认头像地址
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

    @PostMapping("/findUserByUserName")
    public User findUserByUserName(User user)throws Exception {
        return userService.findUserByUserName(user);
    }

    @PostMapping("/findUserByUserId")
    public User findUserByUserId(int userId)throws Exception {
        return userService.findUserByUserId(userId);
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

    @PostMapping("/updateNickName")
    public String updateNickName(User user)throws Exception {
        boolean checkOnlyNickName = userService.checkOnlyNickName(user);
        if(checkOnlyNickName){
            return "nickNameExist";
        }else {
            boolean result = userService.updateNickNameByUserId(user);
            if(result){
                return "updateNickNameSuccess";
            }else {
                return "updateNickNameFail";
            }
        }
    }

    @PostMapping("/updateSex")
    public String updateSex(User user)throws Exception {
            boolean result = userService.updateSexByUserId(user);
            if(result){
                return "updateSexSuccess";
            }else {
                return "updateSexFail";
            }
    }

    @PostMapping("/updateBirth")
    public String updateBirth(String newBirth,User user)throws Exception {
        Date birth=new SimpleDateFormat("yyyy-MM-dd").parse(newBirth);
        user.setBirth(birth);
            boolean result = userService.updateBirthByUserId(user);
            if(result){
                return "updateBirthSuccess";
            }else {
                return "updateBirthFail";
            }
    }

    @PostMapping("/insertSchool")
    public String updateSchool(User user)throws Exception {
            boolean result = userService.insertSchoolByUserId(user);
            if(result){
                return "insertSchoolSuccess";
            }else {
                return "insertSchoolFail";
            }
    }

    @PostMapping("/updatePhoto")
    public String updatePhoto(User user,@RequestParam("file") MultipartFile file, @RequestParam("photoOriginalUrl")String photoOriginalUrl,@RequestParam("userId")int userId, HttpServletRequest request)throws Exception{
        String photo = uploadUtil.uploadOneFileUpdate(file,"D://upload_2021//shopsystem",request,"user",photoOriginalUrl);
        System.out.println("更新后的图片："+photo);
        user.setProfilePhoto(photo);
        user.setUserId(userId);
        boolean result = userService.updatePhotoByUserId(user);
        if (result){
            return photo;
        }else {
            return "updateFail";
        }


    }







}

package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.Admin;
import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.service.AdminService;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommodityService commodityService;

    /*当你访问：localhost:8088/toSign  可以访问到templates下的sign.html*/
    @RequestMapping("/toSign")
    public String toSign()throws Exception{
        return "sign";
    }

    //http://localhost:8088/shopsystem/admin/login1 只能post访问
    @PostMapping("/login")
    public ModelAndView login(ModelAndView mav, Admin admin, HttpSession session)throws Exception{
        String result = adminService.login(admin);
        switch (result){
            case "nameErr":
                mav.setViewName("sign");
                mav.addObject("msg","用户名不存在！");
                break;
            case "pwdErr":
                mav.setViewName("sign");
                mav.addObject("msg","密码错误！");
                break;
            case "success":
                mav.setViewName("adminIndex");//设置跳转页面
                Admin adminLogin = adminService.findAdminByAdminName(admin);
                session.setAttribute("adminLogin",adminLogin);
                break;
        }
        return mav;
    }

    //http://localhost:8088/shopsystem/admin/login1 只能post访问
    @GetMapping("/loginOut")
    public String loginOut(ModelAndView mav)throws Exception{
        mav.clear();
        return "sign";
    }

}

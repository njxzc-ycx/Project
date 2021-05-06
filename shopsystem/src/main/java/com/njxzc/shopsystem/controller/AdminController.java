package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.Admin;
import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.service.*;
import org.apache.http.HttpRequest;
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

    @Autowired
    private DisMessageService disMessageService;

    @Autowired
    private DiscussService discussService;

    @Autowired
    private UserOrderService userOrderService;

    /*当你访问：localhost:8088/toSign  可以访问到templates下的sign.html*/
    @RequestMapping("/toSign")
    public String toSign()throws Exception{
        return "sign";
    }

    @RequestMapping("/adminIndex")
    public String adminIndex()throws Exception{
        return "manager";
    }

    @RequestMapping("/welcome")
    public ModelAndView welcome(ModelAndView mav)throws Exception{
        int comFlagUpCounts = commodityService.findComCountByFlag();
        int userCounts = userService.findUserCount();
        int discussCounts = discussService.findDiscussCount();
        int replayCounts = disMessageService.findDisMessageCounts()+disMessageService.findDisReplayCounts();
        int orderCompleteCounts = userOrderService.findOrderCompleteCounts();
        mav.addObject("comFlagUpCounts",comFlagUpCounts);
        mav.addObject("userCounts",userCounts);
        mav.addObject("discussCounts",discussCounts);
        mav.addObject("replayCounts",replayCounts);
        mav.addObject("orderCompleteCounts",orderCompleteCounts);
        mav.setViewName("welcome");
        return mav;
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
                mav.setViewName("manager");//设置跳转页面
                Admin adminLogin = adminService.findAdminByAdminName(admin);
                session.setAttribute("adminLogin",adminLogin);
                break;
        }
        return mav;
    }

    //http://localhost:8088/shopsystem/admin/login1 只能post访问
    @GetMapping("/loginOut")
    public String loginOut(ModelAndView mav, HttpSession session)throws Exception{
        mav.clear();
        session.removeAttribute("adminLogin");
        return "sign";
    }

}

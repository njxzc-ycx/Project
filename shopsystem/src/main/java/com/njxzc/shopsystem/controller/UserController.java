package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
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
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;//这里的userService大小写要和 @Service("userService")一致

    @Autowired
    CommodityService commodityService;

    //@GetMapping("/toAllUser") 指定访问请求方式 只能为get请求
    //访问地址：http://localhost:8088/shopsystem/user/toAllUser
    @GetMapping("/toAllUser")
    public ModelAndView toAllUser(ModelAndView mav)throws Exception{
        //调用业务逻辑中查询所有用户的方法
        List<User> users = userService.findAllUsers();
        mav.setViewName("alluser");//设置跳转页面
        mav.addObject("users",users);//将查询到的信息放入Request域中
        return mav;
    }

    @GetMapping("/toAllUser2")
    public ModelAndView toAllUser2(ModelAndView mav)throws Exception{
        //调用业务逻辑中查询所有用户的方法
        List<User> users = userService.findAllUsers2();
        mav.setViewName("alluser");//设置跳转页面
        mav.addObject("users",users);//将查询到的信息放入Request域中
        return mav;
    }

    //http://localhost:8088/shopsystem/user/login1 只能post访问
    @PostMapping("/login1")
    public ModelAndView login1(ModelAndView mav,User user)throws Exception {
        String result = userService.login(user);
        switch (result) {
            case "nameErr":
                mav.setViewName("signin");
                mav.addObject("msg", "用户名错误！");
                break;
            case "pwdErr":
                mav.setViewName("signin");
                mav.addObject("msg", "密码错误！");
                break;
            case "locked":
                mav.setViewName("signin");
                mav.addObject("msg", "你的账号已被封禁，请联系客服！");
                break;
            case "success":
                //该段是普通用户登录之后的效果，登录后要能看到，登录后才能看的内容，可能是商品、电影、具体信息
                //能够查询出该用户有权限看到的信息（商品为例，可以看到按种类区分的商品、按照热度排名的商品、可能是各种各样的）
                List<User> users = userService.findAllUsers2();
                mav.setViewName("alluser");//设置跳转页面
                mav.addObject("users", users);//将查询到的信息放入Request域中
                break;

        }
        return mav;
    }

        //http://localhost:8088/shopsystem/user/login1 只能post访问
        @PostMapping("/login2")
        public ModelAndView login2(ModelAndView mav, User user, HttpSession session)throws Exception{
            String result = userService.login(user);
            switch (result){
                case "nameErr":
                    mav.setViewName("signin");
                    mav.addObject("msg","用户名错误！");
                    break;
                case "pwdErr":
                    mav.setViewName("signin");
                    mav.addObject("msg","密码错误！");
                    break;
                case "locked":
                    mav.setViewName("signin");
                    mav.addObject("msg","你的账号已被封禁，请联系客服！");
                    break;
                case "success":
                    //该段是普通用户登录之后的效果，登录后要能看到，登录后才能看的内容，可能是商品、电影、具体信息
                    //能够查询出该用户有权限看到的信息（商品为例，可以看到按种类区分的商品、按照热度排名的商品、可能是各种各样的）
                    //之前普通用户查询的是所有商品
                    //List<Commodity> allCommodities = commodityService.findAllCommodity();
                    //普通用户只能查询已上架的商品
                    Commodity commodity = new Commodity();
                    commodity.setFlag(1);//我们设计的商品状态为1 是上架商品
                    List<Commodity> hitsCommodities = commodityService.findCommodityByHits();
                    List<Commodity> allCommodities = commodityService.findAllComByCondition(commodity);
                    System.out.println(allCommodities);
                    System.out.println(hitsCommodities);
                    mav.setViewName("commodityIndex");//设置跳转页面
                    mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
                    mav.addObject("hitsCommodities",hitsCommodities);//将查询到的信息放入Request域中
                    User loginUser = userService.findUserByUserName(user);//登录的用户信息 放入Session
                    session.setAttribute("loginUser",loginUser);
                    break;

            }
            return mav;
    }


}

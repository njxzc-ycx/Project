package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.UserOrder;
import com.njxzc.shopsystem.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("order")
public class orderController {

    @Autowired
    UserOrderService userOrderService;

    @GetMapping("/findOrderDesc/{userId}")
    public ModelAndView findOrderDesc(ModelAndView mav, @PathVariable("userId")int userId)throws Exception{
        System.out.println("根据用户编号查询订单详情，当前用户编号:"+userId);
        List<UserOrder> userOrderList = userOrderService.findOrderAndAddress(userId);
        mav.addObject("orders",userOrderList);//订单信息放在orders中 当前用户信息，仍然在session中
        mav.setViewName("orderIndex");//跳转到所有订单页面
        return mav;
    }

    //测试查询1002 编号数据是否能够查询
    //http://localhost:8088/shopsystem/order/testFindOrderAndAddressJson/1002
    @GetMapping("/testFindOrderAndAddressJson")
    @ResponseBody
    public List<UserOrder> testFindOrderAndAddressJson()throws Exception{
        return userOrderService.findOrderAndAddress(1002);
    }

    //测试查询1000001 编号数据是否能够查询
    //http://localhost:8088/shopsystem/order/testFindOrderDescJson/1000001
    @GetMapping("/testFindOrderDescJson/{orderId}")
    @ResponseBody
    public UserOrder testFindOrderDescJson(@PathVariable("orderId") int orderId)throws Exception{
        return userOrderService.findOrderAndAddressAndDesc(1000001);
    }

    //根据订单编号，查询订单详情
    @GetMapping("/findOrderByOrderId/{orderId}")
    public ModelAndView findOrderByOrderId(ModelAndView mav,@PathVariable("orderId") int orderId)throws Exception{
        System.out.println("订单编号:"+orderId);
        UserOrder userOrder = userOrderService.findOrderAndAddressAndDesc(orderId);
        mav.addObject("order",userOrder);//订单信息放在orders中 当前用户信息，仍然在session中
        mav.setViewName("orderDesc");
        return mav;
    }

    @PostMapping("/buyCom1")
    @ResponseBody
    public String buyCom1(HttpSession session, Commodity commodity,int buyCount)throws Exception{
        String result = userOrderService.buyCom1(session,commodity,buyCount);
        return result;
    }
}

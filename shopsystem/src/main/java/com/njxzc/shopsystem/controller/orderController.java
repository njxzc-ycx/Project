package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.UserOrder;
import com.njxzc.shopsystem.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("order")
public class orderController {

    @Autowired
    UserOrderService userOrderService;

    @GetMapping("/findOrderDesc/{userId}")
    public ModelAndView findOrderDesc(ModelAndView mav, @PathVariable("userId")int userId)throws Exception{
        System.out.println("根据用户编号查询订单详情，当前用户编号:"+userId);
        return mav;
    }

    //测试查询1002 编号数据是否能够查询
    //http://localhost:8088/shopsystem/order/testFindOrderAndAddressJson
    @GetMapping("/testFindOrderAndAddressJson")
    @ResponseBody
    public List<UserOrder> testFindOrderAndAddressJson()throws Exception{
        return userOrderService.findOrderAndAddress(1002);
    }
}

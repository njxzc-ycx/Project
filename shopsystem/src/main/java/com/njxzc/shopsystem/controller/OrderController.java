package com.njxzc.shopsystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.pojo.UserOrder;
import com.njxzc.shopsystem.service.UserOrderService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("order")
public class OrderController {

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

    @RequestMapping("/toAllOrders")
    public String toAllUsers()throws Exception{
        return "allOrders";
    }

    @RequestMapping(value ="/findAllOrders")
    @ResponseBody
    public Map<String,Object> findAllOrders(@RequestParam(required=false,defaultValue="1") int page,
                                          @RequestParam(required=false) int limit) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<UserOrder> allOrders  = userOrderService.findAllOrders();
        System.out.println(allOrders);
        // 使用pageInfo包装查询
        PageInfo<UserOrder> rolePageInfo = new PageInfo<>(allOrders);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @RequestMapping(value="/findOrdersByKey",produces="application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> findOrdersByKey(Integer page, Integer limit,int orderId,String buyerName,String sellerName) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        System.out.println("orderId:"+orderId);
        System.out.println("buyerName:"+buyerName);
        System.out.println("sellerName:"+sellerName);
        PageHelper.startPage(page, limit);
        List<UserOrder> userOrders  = userOrderService.findOrdersByKey(orderId, buyerName, sellerName);
        System.out.println(userOrders);
        // 使用pageInfo包装查询
        PageInfo<UserOrder> rolePageInfo = new PageInfo<>(userOrders);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @GetMapping("/findOrderDetailByOrderId/{orderId}")
    public ModelAndView findOrderDetailByOrderId(ModelAndView mav, @PathVariable("orderId")int orderId)throws Exception{
        UserOrder userOrder = userOrderService.findOrderDetailByOrderId(orderId);
        mav.addObject("orderDetail",userOrder);//订单信息放在orders中 当前用户信息，仍然在session中
        mav.setViewName("orderDetail");//跳转到所有订单页面
        return mav;
    }
}

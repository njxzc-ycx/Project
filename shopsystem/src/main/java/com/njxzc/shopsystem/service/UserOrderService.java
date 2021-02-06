package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.UserOrder;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserOrderService {

    //根据用户编号查询订单及地址
    List<UserOrder> findOrderAndAddress(int userId);

    //根据订单编号查询订单及地址及详情
    UserOrder findOrderAndAddressAndDesc(int orderId);

    //直接购买功能 先判断是否能够购买 比如，用户是否登录、钱是否够用
    String buyCom1(HttpSession session, Commodity commodity,int buyCount);

    //插入订单业务逻辑
    boolean insertUserOrder(UserOrder userOrder);

}

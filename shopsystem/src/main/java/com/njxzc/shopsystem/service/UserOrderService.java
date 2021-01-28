package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.pojo.UserOrder;

import java.util.List;

public interface UserOrderService {

    //根据用户编号查询订单及地址
    List<UserOrder> findOrderAndAddress(int userId);


}

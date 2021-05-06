package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.OrderDetail;
import com.njxzc.shopsystem.pojo.UserOrder;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserOrderService {

    //根据用户编号查询订单及地址
    List<UserOrder> findOrderAndAddress(int userId);

    //根据订单编号查询订单及地址及详情
    UserOrder findOrderAndAddressAndDesc(int orderId);

    //直接购买功能 先判断是否能够购买 比如，用户是否登录、钱是否够用
    String buyCom1(HttpSession session, Commodity commodity,int buyCount);

    int buyCom(UserOrder userOrder,OrderDetail orderDetail);

    //插入订单业务逻辑
    boolean insertUserOrder(UserOrder userOrder);

    //查询重复商品订单
    boolean selectIsExistComOrder(int userId,int comId);

    List<UserOrder> findSellerOrder(int sellerId);

    boolean sellerManageOrder(UserOrder userOrder);

    boolean getOrder(UserOrder userOrder);

    //卖家查询自己购买的商品订单
    List<UserOrder> findBuyerOrder(int userId);

    //查询成交订单数
    int findOrderCompleteCounts();

    //查询所有订单
    List<UserOrder> findAllOrders();

    //根据条件查询订单
    List<UserOrder> findOrdersByKey(int orderId,String sellerName,String buyerName);

    //根据订单编号查询订单详情
    UserOrder findOrderDetailByOrderId(@Param("orderId") int orderId);

}

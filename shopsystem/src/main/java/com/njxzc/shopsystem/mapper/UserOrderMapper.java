package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.OrderDetail;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.pojo.UserOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrderMapper {

    /*1.根据用户编号查询所有订单信息
    * 一个用户可以有多个订单 用户对订单 一对多
    *
    * 订单对用户 一个订单只能属于一个用户 一对一
    * 订单对订单详情 一个订单可以有多条详情记录 订单对订单详情 一对多
    * 订单对收货地址 一个订单只可能有一个地址 一对一
    *
    * 以订单作为主体（作为返回值类型）
    * */

    /*考虑到实际情况 以及页面 我们以 用户作为主体，先作为返回值*/
    List<UserOrder> findOrderAndAddress(int userId);

    //根据订单号及其详情、地址等一个订单号只可能查出一个订单，但是订单可以有多个详情条目
    UserOrder findOrderAndAddressAndDesc(int orderId);

    //新增订单信息
    int insertUserOrder(UserOrder userOrder);

    //查询重复商品订单
    int selectIsExistComOrder(@Param("userId") int userId,@Param("comId") int comId);

    //卖家查询自己售出商品的订单
    List<UserOrder> findSellerOrder(@Param("sellerId") int sellerId);

    //卖家管理订单，改变订单状态
    int sellerManageOrder(UserOrder userOrder);

    int getOrder(UserOrder userOrder);

    //卖家查询自己购买的商品订单
    List<UserOrder> findBuyerOrder(@Param("userId") int userId);

    //查询成交订单数
    int findOrderCompleteCounts();

    //查询所有订单
    List<UserOrder> findAllOrders();

    //根据条件查询订单
    List<UserOrder> findOrdersByKey(@Param("orderId") int orderId,@Param("buyerName") String buyerName,@Param("sellerName") String sellerName);

    //根据订单编号查询订单详情
    UserOrder findOrderDetailByOrderId(@Param("orderId") int orderId);
}

package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.AddressMapper;
import com.njxzc.shopsystem.mapper.UserOrderMapper;
import com.njxzc.shopsystem.pojo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Transactional
@Service("userOrderService")
public class UserOrderServiceImpl implements UserOrderService {

    @Resource
    UserOrderMapper userOrderMapper;

    @Resource
    AddressMapper addressMapper;

    @Resource
    OrderDetailService orderDetailService;

    @Resource
    UserService userService;

    @Override
    public List<UserOrder> findOrderAndAddress(int userId) {
        return userOrderMapper.findOrderAndAddress(userId);
    }

    @Override
    public UserOrder findOrderAndAddressAndDesc(int orderId) {
        return userOrderMapper.findOrderAndAddressAndDesc(orderId);
    }

    @Override
    public String buyCom1(HttpSession session, Commodity commodity,int buyCount) {
        if(null!=session.getAttribute("loginUser")){
            User user = (User) session.getAttribute("loginUser");
            double total = buyCount*commodity.getCurrentPrice();
            if(user.getBalance()>=total){
                //1.订单编号如何处理（订单表生成后需要把订单编号添加到订单详情表中）
                int orderId = (int)(Math.random()*1234567890);
                //订单编号使用随机生成 用户编号session获取 地址编号应该用当前用户的默认地址（暂未处理）
                //订单时间使用单签系统时间时间戳 收货时间null
                //总价其实可以计算使用buyCount*commodity.getCurrentPrice(); 备注使用默认值 直接购买
                //处理默认地址
                Address address = new Address();
                int addressId = addressMapper.findDefaultAddressByUserId(user.getUserId()).getAddressId();
                UserOrder userOrder = new UserOrder(orderId,user.getUserId(),addressId,
                        new Timestamp(System.currentTimeMillis()),null,total,"直接购买",1);
                //调用插入方法 将订单信息录入数据库
                boolean b = insertUserOrder(userOrder);
                if (b){
                    //订单生成成功后，再将商品详情记录到订单详情中
                    OrderDetail orderDetail = new OrderDetail(0,orderId,commodity.getComId(),buyCount);
                    boolean addDetailFlag = orderDetailService.insertOrderDetail(orderDetail);
                    if(addDetailFlag){
                        double newBalance = user.getBalance()-total;
                        user.setBalance(newBalance);
                        userService.updateBalanceByUserId(newBalance,user.getUserId());
                        return "success";
                    }else {
                        return "orderDetailFail";//订单详情失败
                    }

                }else {
                    return "userOrderFail1";//订单录入失败
                }

            }else {
                return "noMoney";//购买时，钱不够
            }

        }else {
            return "noLogin";//如果session中没有用户信息，未登录
        }

    }

    @Override
    public int buyCom(UserOrder userOrder, OrderDetail orderDetail) {
        int orderId = getRandom();
        userOrder.setOrderId(orderId);
        boolean result = insertUserOrder(userOrder);
        if (result){
            orderDetail.setOrderId(orderId);
            boolean addDetailFlag = orderDetailService.insertOrderDetail(orderDetail);
            if(addDetailFlag){
                return orderId;
            }else {
                return 0;//订单详情失败
            }

        }else {
            return 0;//订单录入失败
        }
    }


    @Override
    public boolean insertUserOrder(UserOrder userOrder) {
        try {
            int result = userOrderMapper.insertUserOrder(userOrder);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean selectIsExistComOrder(int userId, int comId) {
        try {
            int result = userOrderMapper.selectIsExistComOrder(userId,comId);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public List<UserOrder> findSellerOrder(int sellerId) {
        return userOrderMapper.findSellerOrder(sellerId);
    }

    @Override
    public boolean sellerManageOrder(UserOrder userOrder) {
        try {
            int result = userOrderMapper.sellerManageOrder(userOrder);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean getOrder(UserOrder userOrder) {
        try {
            int result = userOrderMapper.getOrder(userOrder);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public List<UserOrder> findBuyerOrder(int userId) {
        return userOrderMapper.findBuyerOrder(userId);
    }

    @Override
    public int findOrderCompleteCounts() {
        return userOrderMapper.findOrderCompleteCounts();
    }

    @Override
    public List<UserOrder> findAllOrders() {
        return userOrderMapper.findAllOrders();
    }

    @Override
    public List<UserOrder> findOrdersByKey(int orderId, String sellerName, String buyerName) {
        return userOrderMapper.findOrdersByKey(orderId,sellerName,buyerName);
    }

    @Override
    public UserOrder findOrderDetailByOrderId(int orderId) {
        return userOrderMapper.findOrderDetailByOrderId(orderId);
    }

    public static int getRandom() {
        Random random = new Random();
        // 随机数的量 自由定制，这是9位随机数
        Integer r = random.nextInt(900000) + 100000;

        /*// 返回  13位时间
        Long timeMillis = System.currentTimeMillis();*/

        // 返回  17位时间
        DateFormat sdf = new SimpleDateFormat("SSS");
        String timeStr = sdf.format(new Date());

        return Integer.parseInt(timeStr + r);

    }
}

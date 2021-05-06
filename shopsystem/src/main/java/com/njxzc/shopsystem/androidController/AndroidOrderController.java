package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.mapper.UserOrderMapper;
import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.OrderDetail;
import com.njxzc.shopsystem.pojo.UserOrder;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("androidOrder")
public class AndroidOrderController {

    @Autowired
    UserOrderService userOrderService;

    @Autowired
    CommodityService commodityService;

    @PostMapping("/buyCom")
    @ResponseBody
    public int buyCom(UserOrder userOrder, OrderDetail orderDetail)throws Exception{
        int userId = userOrder.getUserId();
        int comId = orderDetail.getComId();
        boolean check = userOrderService.selectIsExistComOrder(userId,comId);
        if (check){
            return 1;
        }else {
            int result = userOrderService.buyCom(userOrder, orderDetail);
            return result;
        }
    }

    @PostMapping("/findSellerOrder")
    public List<UserOrder> findSellerOrder(int sellerId)throws Exception{
        return userOrderService.findSellerOrder(sellerId);
    }

    @PostMapping("/sellerManageOrder")
    public String sellerManageOrder(UserOrder userOrder)throws Exception{
        boolean result = userOrderService.sellerManageOrder(userOrder);
        if (result){
            return "success";
        }else {
            return "fail";
        }
    }

    @PostMapping("/getOrder")
    public String getOrder(UserOrder userOrder)throws Exception{
        boolean result = userOrderService.getOrder(userOrder);
        if (result){
            return "success";
        }else {
            return "fail";
        }
    }

    @PostMapping("/findBuyerOrder")
    public List<UserOrder> findBuyerOrder(int userId)throws Exception{
        return userOrderService.findBuyerOrder(userId);
    }

    @PostMapping("/updateCommodityInfoAndUserOrderInfoByComId")
    public String updateCommodityInfoAndUserOrderInfoByComId(Commodity commodity, UserOrder userOrder){
        String result = commodityService.updateCommodityInfoByComId(commodity);
        boolean check = userOrderService.sellerManageOrder(userOrder);
        if (result=="flagWithdraw"||result=="changeCount"&&check){
            return "success";
        }else {
            return "fail";
        }
    }


}

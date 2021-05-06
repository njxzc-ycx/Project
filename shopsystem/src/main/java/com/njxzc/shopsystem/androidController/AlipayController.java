package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.service.AlipayService;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserOrderService;
import com.njxzc.shopsystem.utils.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("alipay")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private CommodityService commodityService;

    /**
     * 创建订单
     */
    @PostMapping("/createOrder")
    public ResultMap createOrder(@RequestParam String orderNo,
                                 @RequestParam double amount,
                                 @RequestParam String body,
                                 @RequestParam int comId,
                                 @RequestParam int count) {
        try {
            // 1、验证商品是否被他人买走或者是否数量不够
            Commodity commodity = commodityService.findCommodityByComId(comId);
            int nowcount = commodity.getCount();
            int flag = commodity.getFlag();
            if (flag==2||flag==3){
                return ResultMap.ok().put("data","error1");
            }else if (nowcount<count){
                return ResultMap.ok().put("data","error2");
            }else {
                // 2、创建支付宝订单
                String orderStr = alipayService.createOrder(orderNo, amount, body);
                return ResultMap.ok().put("data", orderStr);
            }
        } catch (Exception e) {

            return ResultMap.error("订单生成失败");
        }
    }

    /**
     * 支付异步通知
     * 接收到异步通知并验签通过后，一定要检查通知内容
     */
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request) {
        // 验证签名
        boolean flag = alipayService.rsaCheckV1(request);
        if (flag) {
            String tradeStatus = request.getParameter("trade_status"); // 交易状态
            String outTradeNo = request.getParameter("out_trade_no"); // 商户订单号
            String tradeNo = request.getParameter("trade_no"); // 支付宝订单号
            boolean notify = alipayService.notify(tradeStatus, outTradeNo, tradeNo);
            if(notify){
                return "success";
            }
        }
        return "fail";
    }
}

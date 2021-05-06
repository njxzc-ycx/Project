package com.njxzc.shopsystem.service;

import com.alipay.api.AlipayApiException;

import javax.servlet.http.HttpServletRequest;

public interface AlipayService {
    /**
     * @Description: 创建支付宝订单
     * @param orderNo: 订单编号
     * @param amount: 实际支付金额
     * @param body: 订单描述
     * @Author:
     * @Date: 2021/3/11
     * @return
     */
    String createOrder(String orderNo, double amount, String body) throws AlipayApiException;

    /**
     * @Description:
     * @param tradeStatus: 支付宝交易状态
     * @param orderNo: 订单编号
     * @param tradeNo: 支付宝订单号
     * @Author:
     * @Date: 2021/3/11
     * @return
     */
    boolean notify(String tradeStatus, String orderNo, String tradeNo);

    /**
     * @Description: 校验签名
     * @param request
     * @Author:
     * @Date: 2021/3/11
     * @return
     */
    boolean rsaCheckV1(HttpServletRequest request);
}

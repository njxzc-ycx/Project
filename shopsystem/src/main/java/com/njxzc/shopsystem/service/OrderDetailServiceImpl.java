package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.OrderDetailMapper;
import com.njxzc.shopsystem.pojo.OrderDetail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("orderDetailService")
public class OrderDetailServiceImpl implements OrderDetailService {

    @Resource
    OrderDetailMapper orderDetailMapper;

    @Override
    public boolean insertOrderDetail(OrderDetail orderDetail) {
        try {
            int result = orderDetailMapper.insertOrderDetail(orderDetail);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}

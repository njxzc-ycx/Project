package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.UserOrderMapper;
import com.njxzc.shopsystem.pojo.UserOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("userOrderService")
public class UserOrderServiceImpl implements UserOrderService {

    @Resource
    UserOrderMapper userOrderMapper;

    @Override
    public List<UserOrder> findOrderAndAddress(int userId) {
        return userOrderMapper.findOrderAndAddress(userId);
    }
}

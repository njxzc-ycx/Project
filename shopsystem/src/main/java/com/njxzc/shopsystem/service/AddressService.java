package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressService {

    //查询默认地址
    Address findDefaultAddressByUserId(int userId);

    //无默认地址查询第一个
    Address findFirstAddressCountByUserId(int userId);

    //查询所有地址
    List<Address> findAllAddressByUserId(int userId);

    //新增地址
    boolean addAddress(Address address);

    //如果有新地址设为默认 则将其他地址设为非默认
    boolean updateOtherAddressesNoDefault(int userId);

    //查询地址个数
    int findAddressCount(int userId);

    //根据地址编号查询地址
    Address findAddressByAddressId(Address address);

    //修改地址
    boolean updateAddress(Address address);
}

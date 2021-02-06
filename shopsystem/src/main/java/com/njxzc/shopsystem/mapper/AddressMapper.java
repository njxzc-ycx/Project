package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.Address;

public interface AddressMapper {

    Address findDefaultAddressByUserId(int addressId);
}

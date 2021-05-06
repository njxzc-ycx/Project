package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.AddressMapper;
import com.njxzc.shopsystem.pojo.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("addressService")
public class AddressServiceImpl implements AddressService {

    @Resource
    AddressMapper addressMapper;

    @Override
    public Address findDefaultAddressByUserId(int userId) {
        return addressMapper.findDefaultAddressByUserId(userId);
    }

    @Override
    public Address findFirstAddressCountByUserId(int userId) {
        return addressMapper.findFirstAddressCountByUserId(userId);
    }

    @Override
    public List<Address> findAllAddressByUserId(int userId) {
        return addressMapper.findAllAddressByUserId(userId);
    }

    @Override
    public boolean addAddress(Address address) {
        try {
            int result = addressMapper.addAddress(address);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateOtherAddressesNoDefault(int userId) {
        try {
            int result = addressMapper.updateOtherAddressesNoDefault(userId);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public int findAddressCount(int userId) {
        return addressMapper.findAddressCount(userId);
    }

    @Override
    public Address findAddressByAddressId(Address address) {
        return addressMapper.findAddressByAddressId(address);
    }

    @Override
    public boolean updateAddress(Address address) {
        try {
            int result = addressMapper.updateAddress(address);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}

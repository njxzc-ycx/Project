package com.njxzc.shopsystem.androidController;


import com.njxzc.shopsystem.pojo.Address;
import com.njxzc.shopsystem.service.AddressService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("androidAddress")
public class AndroidAddressController {

    @Resource
    AddressService addressService;

    @PostMapping("/findAllAddressByUserId")
    public List<Address> findAllAddressByUserId(int userId){
        return addressService.findAllAddressByUserId(userId);
    }

    @PostMapping("/findAllAddressCount")
    public int findAllAddressCount(int userId){
        return addressService.findAddressCount(userId);
    }

    @PostMapping("/findAddressByAddressId")
    public Address findAddressByAddressId(Address address){
        return addressService.findAddressByAddressId(address);
    }

    @PostMapping("/addAddress")
    public String addAddress(Address address) {
        System.out.println(address);
        int defaultAddress = address.getDefaultAddress();
        int userId = address.getUserId();
        int count = addressService.findAddressCount(userId);
        if (defaultAddress == 1) {
            if (count==0){
                boolean check = addressService.addAddress(address);
                if (check) {
                    return "addSuccess";
                } else {
                    return "addFail";
                }
            }else {
                //如果新增的地址为默认 则 其他改为非默认
                boolean checkNoDefault = addressService.updateOtherAddressesNoDefault(userId);
                if (checkNoDefault) {
                    boolean check = addressService.addAddress(address);
                    if (check) {
                        return "addSuccess";
                    } else {
                        return "addFail";
                    }
                } else {
                    return "noDefaultFail";
                }
            }
        } else{
            //如果新增的不是默认 则直接添加
                boolean check = addressService.addAddress(address);
                if (check) {
                    return "addSuccess";
                } else {
                    return "addFail";
                }
        }

    }

    @PostMapping("/updateAddress")
    public String updateAddress(Address address) {
        int defaultAddress = address.getDefaultAddress();
        int userId = address.getUserId();
        if (defaultAddress == 1) {
                //如果新增的地址为默认 则 其他改为非默认
                boolean checkNoDefault = addressService.updateOtherAddressesNoDefault(userId);
                if (checkNoDefault) {
                    boolean check = addressService.updateAddress(address);
                    if (check) {
                        return "updateSuccess";
                    } else {
                        return "updateFail";
                    }
                } else {
                    return "noDefaultFail";
                }

        } else{
            //如果新增的不是默认 则直接添加
            boolean check = addressService.updateAddress(address);
            if (check) {
                return "updateSuccess";
            } else {
                return "updateFail";
            }
        }

    }

    @PostMapping("/findDefaultAddressByUserId")
    public Address findDefaultAddressByUserId(int userId){
        Address address = addressService.findDefaultAddressByUserId(userId);
        if (address==null) {
            System.out.println("无默认地址");
            address = addressService.findFirstAddressCountByUserId(userId);
        }
            return address;

    }

}

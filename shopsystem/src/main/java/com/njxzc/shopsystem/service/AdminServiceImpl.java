package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.AdminMapper;
import com.njxzc.shopsystem.pojo.Admin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("adminService")
public class AdminServiceImpl implements AdminService {

    @Resource
    AdminMapper adminMapper;

    @Override
    public String login(Admin admin) {
        Admin getAdmin = adminMapper.findAdminByAdminName(admin);
        if(null==getAdmin){
            return "nameErr";
        }else {
            if (getAdmin.getAdminPwd().equals(admin.getAdminPwd())){
                    return "success";

            }else {
                return "pwdErr";
            }
        }
    }

    @Override
    public Admin findAdminByAdminName(Admin admin) {
        return adminMapper.findAdminByAdminName(admin);
    }
}

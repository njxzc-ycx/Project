package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.Admin;

public interface AdminMapper {

    //根据管理员用户名查询
    Admin findAdminByAdminName(Admin admin);


}

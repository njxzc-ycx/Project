package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Admin;

public interface AdminService {

    //管理员登录功能业务逻辑
    String login(Admin admin);

    //根据管理员用户名查询
    Admin findAdminByAdminName(Admin admin);
}

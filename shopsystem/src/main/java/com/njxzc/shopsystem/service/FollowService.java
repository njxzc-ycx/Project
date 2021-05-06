package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.pojo.Follow;

import java.util.List;

public interface FollowService {

    //根据用户编号查询关注
    List<Follow> findFollowByUserId(Follow follow);

    //查询用户是否被关注
    boolean findFollowByFollowerIdAndUserId(Follow follow);

    //关注
    boolean joinFollowed(Follow follow);

    //取消关注
    boolean cancelFollowed(Follow follow);
}

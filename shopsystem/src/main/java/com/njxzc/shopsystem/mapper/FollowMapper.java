package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.pojo.Follow;

import java.util.List;

public interface FollowMapper {

    //根据用户编号查询关注
    List<Follow> findFollowByUserId(Follow follow);

    //查询用户是否被关注
    int findFollowByFollowerIdAndUserId(Follow follow);

    //关注
    int joinFollowed(Follow follow);

    //取消关注
    int cancelFollowed(Follow follow);


}

package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Collect;

import java.util.List;

public interface CollectService {

    //根据用户编号查询收藏
    List<Collect> findCollectByUserId(Collect collect);

    //查询商品是否为收藏
    boolean findCollectByComIdAndUserId(Collect collect);

    //取消收藏
    boolean cancelCollected(Collect collect);

    //加入收藏
    boolean joinCollected(Collect collect);
}

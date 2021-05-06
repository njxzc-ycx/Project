package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.pojo.Commodity;

import java.util.List;
import java.util.Map;

public interface CollectMapper {

    //根据用户编号查询收藏
    List<Collect> findCollectByUserId(Collect collect);

    //查询商品是否为收藏
    int findCollectByComIdAndUserId(Collect collect);

    //取消收藏
    int cancelCollected(Collect collect);

    //加入收藏
    int joinCollected(Collect collect);


}

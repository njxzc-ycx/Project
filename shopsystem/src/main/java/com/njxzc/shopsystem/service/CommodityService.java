package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Commodity;

import java.util.List;
import java.util.Map;

public interface CommodityService {

    //查询所有商品业务逻辑
    List<Commodity> findAllCommodity();

    //查询所有商品记录数
    int findAllComCounts(Map<String, Object> parameter);

    //查询所有商品
    List<Commodity> findAllComByPage(Map<String, Object> parameter);

    //根据商品热度排序查询业务逻辑
    List<Commodity> findCommodityByHits();

    //根据商品编号查询商品业务逻辑
    Commodity findCommodityByComId(int comId);

    //解析图片
    List<String> dealComImageOther(String comImageOther);

    //商品发布
    boolean addCommodity(Commodity commodity);

    //删除商品
    boolean deleteCommodity(int comId);

    //根据条件查询商品
    List<Commodity> findAllComByCondition(Commodity commodity);

    //修改商品状态
    boolean updateComFlagByComId(Commodity commodity);

    //修改商品（根据录入条件不同修改商品）
    boolean updateComByComId(Commodity commodity);

    //根据关键字 查询商品信息
    List<Commodity> findComByKey(String key);

}

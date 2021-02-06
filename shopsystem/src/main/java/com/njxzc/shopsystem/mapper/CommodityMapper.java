package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommodityMapper {

    //查询所有商品
    List<Commodity> findAllCommodity();

    //查询所有商品记录数
    int findAllComCounts(Map<String, Object> parameter);

    //查询所有商品
    List<Commodity> findAllComByPage(Map<String, Object> parameter);

    //根据商品热度排序查询
    List<Commodity> findCommodityByHits();

    //根据商品编号查询商品
    Commodity findCommodityByComId(int comId);

    //商品添加方法
    int addCommodity(Commodity commodity);

    //删除商品
    int deleteCommodity(int comId);

    //根据用户输入条件，查询商品
    List<Commodity> findAllComByCondition(Commodity commodity);

    //商品上架（根据商品编号，修改商品状态）
    int updateComFlagByComId(Commodity commodity);

    //根据输入内容修改商品信息
    int updateComByComId(Commodity commodity);

    //根据关键字查询商品
    List<Commodity> findComByKey(@Param("key") String key);



}

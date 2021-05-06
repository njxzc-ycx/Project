package com.njxzc.shopsystem.mapper;

import com.njxzc.shopsystem.pojo.Commodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommodityMapper {

    //查询所有商品
    List<Commodity> findAllCommodity();

    //根据用户学校查询同校商品
    List<Commodity> findAllCommodityBySchool(@Param("school") String school);

    //根据卖家id查询 所发布的商品
    List<Commodity> findMyCommodityBySellerId(Commodity commodity);

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

    //点击增加点击量
    int addHitsByComId(@Param("comId") int comId);

    //查询点击量
    int findHitsByComId(@Param("comId") int comId);

    //增加收藏量
    int addCollectsByComId(@Param("comId") int comId);

    //减少收藏量
    int reduceCollectsByComId(@Param("comId") int comId);

    //根据第三级分类查询商品
    List<Commodity> findComByThirdId(@Param("thirdId") int thirdId);

    //根据商品编号更新商品信息
    int updateCommodityByComId(Commodity commodity);

    //购买后根据商品编号更新商品数量
    int updateCommodityCountByComId(Commodity commodity);

    //查询上架商品数量
    int findComCountByFlag();

    //管理员查询所有商品
    List<Commodity> findAllCommoditys();

    //管理员查询所有待审核商品
    List<Commodity> findallAuditCommoditys();

    //根据时间、关键字查询商品
    List<Commodity> adminFindCom(@Param("start") String start,@Param("end") String end
            ,@Param("key") String key,@Param("flag") int flag);











}

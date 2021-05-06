package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.UserOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommodityService {

    //查询所有商品业务逻辑
    List<Commodity> findAllCommodity();

    //根据用户学校查询同校商品
    List<Commodity> findAllCommodityBySchool(String school);

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

    //修改商品状态
    boolean withdrawComFlagByComId(Commodity commodity);

    //修改商品（根据录入条件不同修改商品）
    boolean updateComByComId(Commodity commodity);

    //根据关键字 查询商品信息
    List<Commodity> findComByKey(String key);

    //点击增加点击数
    boolean addHitsByComId(int comId);

    //查询点击数
    int findHitsByComId(int comId);

    //增加收藏量
    boolean addCollectsByComId(int comId);

    //减少收藏量
    boolean reduceCollectsByComId(int comId);

    //根据第三级分类查询商品
    List<Commodity> findComByThirdId(int thirdId);

    //根据卖家id查询 所发布的商品
    List<Commodity> findMyCommodityBySellerId(Commodity commodity);

    //根据商品编号更新商品信息
    boolean updateCommodityByComId(Commodity commodity);

    //付款成功后根据商品编号更新商品数量，如果商品数量为0 则下架商品
    String updateCommodityInfoByComId(Commodity commodity);

    //查询上架商品数量
    int findComCountByFlag();

    //管理员查询所有商品
    List<Commodity> findAllCommoditys();

    //管理员查询所有待审核商品
    List<Commodity> findallAuditCommoditys();

    //根据时间、关键字查询商品
    List<Commodity> adminFindCom(String start,String end,String key,int flag);




}

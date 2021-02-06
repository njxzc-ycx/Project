package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.CommodityMapper;
import com.njxzc.shopsystem.pojo.Commodity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
@Service("commodityService")
public class CommodityServiceImpl implements CommodityService {

    @Resource
    CommodityMapper commodityMapper;

    @Override
    public List<Commodity> findAllCommodity() {
        System.out.println("查询所有商品");
        return commodityMapper.findAllCommodity();
    }

    @Override
    public int findAllComCounts(Map<String, Object> parameter) {
        return commodityMapper.findAllComCounts(parameter);
    }

    @Override
    public List<Commodity> findAllComByPage(Map<String, Object> parameter) {
        return commodityMapper.findAllComByPage(parameter);
    }

    @Override
    public List<Commodity> findCommodityByHits() {
        System.out.println("根据热度查询商品");
        return commodityMapper.findCommodityByHits();
    }

    @Override
    public Commodity findCommodityByComId(int comId) {
        System.out.println("根据商品编号查询商品");
        return commodityMapper.findCommodityByComId(comId);
    }

    //将数据库中 ComImageOther 数据为 http://localhost:8088/shopsystem/resources/image/hwMate30pro.jpg http://localhost:8088/shopsystem/resources/image/ip12.jpg http://localhost:8088/shopsystem/resources/image/hwMate30pro.jpg
    // 改为List<String>数据结构 改为["http://localhost:8088/shopsystem/resources/image/hwMate30pro.jpg http://localhost:8088/shopsystem/resources/image/ip12.jpg http://localhost:8088/shopsystem/resources/image/hwMate30pro.jpg"]
    //可以通过 urlList[0] 获取 。。。。。
    @Override
    public List<String> dealComImageOther(String comImageOther) {
        if(null==comImageOther){
            return null;
        }else {
            String imageUrls[] = comImageOther.split(" ");//将String字符串解析为数组
            List<String> urlList = new ArrayList<>();
            for (String url : imageUrls) {
                urlList.add(url);
            }
            return urlList;
        }
    }

    @Override
    public boolean addCommodity(Commodity commodity) {
        try {
            //注册成功 result会返回大于0的数值
            int result = commodityMapper.addCommodity(commodity);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteCommodity(int comId) {
        try {
            //注册成功 result会返回大于0的数值
            int result = commodityMapper.deleteCommodity(comId);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public List<Commodity> findAllComByCondition(Commodity commodity) {
        System.out.println("根据条件查询商品");
        return commodityMapper.findAllComByCondition(commodity);
    }

    @Override
    public boolean updateComFlagByComId(Commodity commodity) {
        //获取到原始状态是0（待审核） 直接修改为1（上架）
        if(0==commodity.getFlag()){
            commodity.setFlag(1);
        }else if(1==commodity.getFlag()){//原先为上架则修改为2（下架）
            commodity.setFlag(2);
        }else if(2==commodity.getFlag()){//原先为上架则修改为1（上架）
            commodity.setFlag(1);
        }
        try {
            //修改成功 result会返回大于0的数值
            int result = commodityMapper.updateComFlagByComId(commodity);
            System.out.println("操作数据："+result);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public boolean updateComByComId(Commodity commodity) {
        try {
            //修改成功 result会返回大于0的数值
            int result = commodityMapper.updateComByComId(commodity);
            System.out.println("操作数据："+result);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public List<Commodity> findComByKey(String key) {
        System.out.println("根据关键字查询");
        return commodityMapper.findComByKey(key);
    }
}

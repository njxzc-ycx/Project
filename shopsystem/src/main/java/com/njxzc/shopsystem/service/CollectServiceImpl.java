package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.CollectMapper;
import com.njxzc.shopsystem.pojo.Collect;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("collectService")
public class CollectServiceImpl implements CollectService {

    @Resource
    CollectMapper collectMapper;

    @Override
    public List<Collect> findCollectByUserId(Collect collect) {
        return collectMapper.findCollectByUserId(collect);
    }

    @Override
    public boolean findCollectByComIdAndUserId(Collect collect) {
        try {
            //注册成功 result会返回大于0的数值
            int result = collectMapper.findCollectByComIdAndUserId(collect);
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
    public boolean cancelCollected(Collect collect) {
        try {
            //注册成功 result会返回大于0的数值
            int result = collectMapper.cancelCollected(collect);
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
    public boolean joinCollected(Collect collect) {
        try {
            //注册成功 result会返回大于0的数值
            int result = collectMapper.joinCollected(collect);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }
}

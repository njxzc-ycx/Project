package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.DiscussMapper;
import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.DiscussType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("discussService")
public class DiscussServiceImpl implements DiscussService {

    @Resource
    DiscussMapper discussMapper;

    @Override
    public List<Discuss> findDiscussByUserId(Discuss discuss) {
        return discussMapper.findDiscussByUserId(discuss);
    }

    @Override
    public List<Discuss> findMyDiscussByUserId(Discuss discuss) {
        return discussMapper.findMyDiscussByUserId(discuss);
    }

    @Override
    public Discuss findDiscussByDiscussId(Discuss discuss) {
        return discussMapper.findDiscussByDiscussId(discuss);
    }

    @Override
    public List<Discuss> findAllDiscuss() {
        return discussMapper.findAllDiscuss();
    }

    @Override
    public List<Discuss> findDiscussByType(Discuss discuss) {
        return discussMapper.findDiscussByType(discuss);
    }

    @Override
    public List<DiscussType> findAllDiscussType() {
        return discussMapper.findAllDiscussType();
    }

    @Override
    public boolean addDiscuss(Discuss discuss) {
        try {
            //result会返回大于0的数值
            int result = discussMapper.addDiscuss(discuss);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteDiscuss(Discuss discuss) {
        try {
            int result = discussMapper.deleteDiscuss(discuss);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addDisscussUp(Discuss discuss) {
        try {
            //result会返回大于0的数值
            int result = discussMapper.addDisscussUp(discuss);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addDisscussComments(Discuss discuss) {
        try {
            //result会返回大于0的数值
            int result = discussMapper.addDisscussComments(discuss);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addDisscussHits(Discuss discuss) {
        try {
            //result会返回大于0的数值
            int result = discussMapper.addDisscussHits(discuss);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public int findDiscussCount() {
        int discussCounts = discussMapper.findDiscussCount();
        return discussCounts;
    }

    @Override
    public List<Discuss> findAllDiscusses() {
        return discussMapper.findAllDiscusses();
    }

    @Override
    public boolean updateDiscussFlag(int discussId,int flag) {
        if (flag==1){
            flag=2;
        }else if (flag==2){
            flag=1;
        }
        try {
            int result = discussMapper.updateDiscussFlag(discussId,flag);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public List<Discuss> findDiscussesByKey(int flag, int typeId, int discussId, String nickName, String key) {
        return discussMapper.findDiscussesByKey(flag,typeId,discussId,nickName,key);
    }
}

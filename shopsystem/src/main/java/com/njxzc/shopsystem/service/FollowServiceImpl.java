package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.FollowMapper;
import com.njxzc.shopsystem.pojo.Follow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("followService")
public class FollowServiceImpl implements FollowService {

    @Resource
    FollowMapper followMapper;

    @Override
    public List<Follow> findFollowByUserId(Follow follow) {
        return followMapper.findFollowByUserId(follow);
    }

    @Override
    public boolean findFollowByFollowerIdAndUserId(Follow follow) {
        try {
            int result = followMapper.findFollowByFollowerIdAndUserId(follow);
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
    public boolean joinFollowed(Follow follow) {
        try {
            int result = followMapper.joinFollowed(follow);
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
    public boolean cancelFollowed(Follow follow) {
        try {
            int result = followMapper.cancelFollowed(follow);
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

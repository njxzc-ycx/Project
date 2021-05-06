package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.pojo.Follow;
import com.njxzc.shopsystem.service.CollectService;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("androidFollow")
public class AndroidFollowController {

    @Autowired
    FollowService followService;

    @PostMapping("/findFollowByUserId")
    public List<Follow> findFollowByUserId(Follow follow){
        return  followService.findFollowByUserId(follow);
    }

    @PostMapping("/isFollowed")
    public String isFollowed(Follow follow)throws Exception{
        boolean check = followService.findFollowByFollowerIdAndUserId(follow);
        if(check){
            return "isFollowed";
        }else {
            return "noFollowed";
        }
    }

    @PostMapping("/joinFollowed")
    public String joinFollowed(Follow follow)throws Exception{
        boolean check = followService.joinFollowed(follow);
        if(check){
            return "joinSuccess";
        }else {
            return "joinFail";
        }
    }

    @PostMapping("/cancelFollowed")
    public String cancelFollowed(Follow follow)throws Exception{
        boolean check = followService.cancelFollowed(follow);
        if(check){
            return "cancelSuccess";
        }else {
            return "cancelFail";
        }
    }


}

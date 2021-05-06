package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.service.CollectService;
import com.njxzc.shopsystem.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("androidCollect")
public class AndroidCollectController {

    @Autowired
    CollectService collectService;

    @Autowired
    CommodityService commodityService;

    @PostMapping("/findCollectByUserId")
    public List<Collect> findCollectByUserId(Collect collect){
        return  collectService.findCollectByUserId(collect);
    }

    @PostMapping("/isCollected")
    public String isCollected(Collect collect)throws Exception{
        boolean check = collectService.findCollectByComIdAndUserId(collect);
        if(check){
            return "isCollected";
        }else {
            return "noCollected";
        }
    }

    @PostMapping("/cancelCollected")
    public String cancelCollected(Collect collect,int comId)throws Exception{
        boolean check = collectService.cancelCollected(collect);
        boolean checkUpdate = commodityService.reduceCollectsByComId(comId);
        if(check&&checkUpdate){
            return "cancelSuccess";
        }else {
            return "cancelFail";
        }
    }

    @PostMapping("/joinCollected")
    public String joinCollected(Collect collect,int comId)throws Exception{
        boolean check = collectService.joinCollected(collect);
        boolean checkUpdate = commodityService.addCollectsByComId(comId);
        if(check&&checkUpdate){
            return "joinSuccess";
        }else {
            return "joinFail";
        }
    }
}

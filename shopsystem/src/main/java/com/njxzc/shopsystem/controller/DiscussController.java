package com.njxzc.shopsystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.UserOrder;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.DiscussService;
import com.njxzc.shopsystem.service.UserOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("discuss")
public class DiscussController {

    @Autowired
    DiscussService discussService;

    @Autowired
    CommodityService commodityService;


    @RequestMapping("/toAllDiscusses")
    public String toAllDiscusses()throws Exception{
        return "allDiscusses";
    }


    @RequestMapping(value ="/findAllDiscusses")
    @ResponseBody
    public Map<String,Object> findAllDiscusses(@RequestParam(required=false,defaultValue="1") int page,
                                          @RequestParam(required=false) int limit) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Discuss> allDiscusses  = discussService.findAllDiscusses();
        System.out.println(allDiscusses);
        // 使用pageInfo包装查询
        PageInfo<Discuss> rolePageInfo = new PageInfo<>(allDiscusses);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @GetMapping("/toDiscussPic")
    public ModelAndView toDiscussPic(ModelAndView mav,Discuss discuss)throws Exception{
        //1.查询商品信息
        discuss = discussService.findDiscussByDiscussId(discuss);
        //2.解析其他多个图片
        List<String> urlList = commodityService.dealComImageOther(discuss.getDiscussImages());
        mav.addObject("discuss",discuss);
        mav.addObject("urlList",urlList);
        mav.setViewName("discussImages");
        return mav;
    }

    @GetMapping("/updateDiscussFlag")
    @ResponseBody
    public void updateDiscussFlag(int discussId,int flag)throws Exception{
        discussService.updateDiscussFlag(discussId,flag);
    }

    @RequestMapping(value="/findDiscussesByKey",produces="application/json;charset=utf-8")
    @ResponseBody
    public Map<String, Object> findDiscussesByKey(Integer page, Integer limit,
                                                  int flag,int typeId,int discussId,String nickName,String key) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Discuss> discusses  = discussService.findDiscussesByKey(flag, typeId,discussId,nickName,key);
        System.out.println(discusses);
        // 使用pageInfo包装查询
        PageInfo<Discuss> rolePageInfo = new PageInfo<>(discusses);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

}

package com.njxzc.shopsystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.Slideshow;
import com.njxzc.shopsystem.service.SlideshowService;
import com.njxzc.shopsystem.utils.uploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("slideshow")
public class SlideshowController {

    @Autowired
    SlideshowService slideshowService;

    @RequestMapping("/toAllSlideshows")
    public String toAllSlideshows()throws Exception{
        return "allSlideshows";
    }

    @RequestMapping("/toAddSlideshow")
    public String toAddSlideshow()throws Exception{
        return "addSlideshow";
    }

    @RequestMapping("/toUpdateSlideshow")
    public ModelAndView toUpdateSlideshow(ModelAndView mav,int slideshowId, String slideshowUrl)throws Exception{
        mav.addObject("slideshowId",slideshowId);
        mav.addObject("slideshowUrl",slideshowUrl);
        mav.setViewName("updateSlideshow");
        return mav;
    }

    @ResponseBody
    @RequestMapping("/updateSlideshowUrl")
    public JSON  updateSlideshowUrl(MultipartFile file,HttpServletRequest request,int slideshowId) {
        JSONObject json=new JSONObject();
        String slideshowUrl = uploadUtil.uploadOneFile(file,"D://upload_2021//shopsystem",request,"slideshow");
        System.out.println(slideshowUrl);
        boolean result = slideshowService.updateSlideshow(slideshowId,slideshowUrl);
        if (result){
            json.put("code",0);
        }else {
            json.put("code",1);
        }

        return json;

    }

    @ResponseBody
    @RequestMapping("/addSlideshow")
    public JSON  addSlideshow(MultipartFile file,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        String slideshowUrl = uploadUtil.uploadOneFile(file,"D://upload_2021//shopsystem",request,"slideshow");
        System.out.println(slideshowUrl);
        boolean result = slideshowService.addSlideshow(slideshowUrl);
        if (result){
            json.put("code",0);
            json.put("file",slideshowUrl);
        }else {
            json.put("code",1);
        }

        return json;

    }

    @RequestMapping(value ="/findAllSlideshows")
    @ResponseBody
    public Map<String,Object> findAllSlideshows(@RequestParam(required=false,defaultValue="1") int page,
                                               @RequestParam(required=false) int limit) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Slideshow> allSlideshows  = slideshowService.findAllSlideshows();
        System.out.println(allSlideshows);
        // 使用pageInfo包装查询
        PageInfo<Slideshow> rolePageInfo = new PageInfo<>(allSlideshows);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @PostMapping("/updateSlideshowStatus")
    @ResponseBody
    public Map<String,String> updateSlideshowStatus(int slideshowId,int slideshowStatus)throws Exception{
        int counts = slideshowService.findSlideshowedCount();
        Map<String,String> map = new HashMap<String, String>();
        if(counts<=3&&slideshowStatus==1){
            map.put("result","轮播图显示不少于3张");
            return map;
        }else if(counts>=5&&slideshowStatus==2){
            map.put("result","轮播图显示不多于5张");
            return map;
        }else{
            slideshowService.updateSlideshowStatus(slideshowId, slideshowStatus);
        }
        map.put("result","操作成功");
        return map;
    }

    @PostMapping("/deleteSlideshow")
    @ResponseBody
    public void deleteSlideshow(int slideshowId)throws Exception{
        slideshowService.deleteSlideshow(slideshowId);
    }
}

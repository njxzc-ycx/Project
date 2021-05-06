package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.utils.uploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;


@RestController
@RequestMapping("androidCom")
public class AndroidComController {

    @Autowired
    CommodityService commodityService;

    @GetMapping("/list")
    public List<Commodity> list(){
        return commodityService.findAllCommodity();
    }

    @PostMapping("/listSameSchoolCom")
    public List<Commodity> listSameSchoolCom(String school){
        System.out.println(school);
        return commodityService.findAllCommodityBySchool(school);
    }

    @PostMapping("/findComByComId")
    public Commodity findComByComId(int comId){
        return commodityService.findCommodityByComId(comId);
    }

    //根据关键字查询商品
    @PostMapping("/searchComBykey")
    public List<Commodity> searchComBykey(String key){
        return commodityService.findComByKey(key);
    }

    @PostMapping("/findComByThirdId")
    public List<Commodity> findComByThirdId(int thirdId){
        return commodityService.findComByThirdId(thirdId);
    }


    @PostMapping("/addTest")
    @ResponseBody
    public void addTest(Commodity commodity){
        commodityService.addCommodity(commodity);
    }

    @PostMapping("/addHits")
    @ResponseBody
    public int addHits(int comId){
        commodityService.addHitsByComId(comId);
        int hits = commodityService.findHitsByComId(comId);
        return hits;

    }

    @PostMapping("/addCom")
    public String addCom(@RequestParam("imageOtherFiles") MultipartFile[] imageOtherFiles,
                             @RequestParam("imageMainFile") MultipartFile imageMainFile,Commodity commodity,
                             HttpServletRequest request)throws Exception{
        System.out.println(imageOtherFiles);
        String imageOtherUrl = uploadUtil.uploadMutiFiles(imageOtherFiles,"D://upload_2021//shopsystem",request,"commodity");
        System.out.println("更新后的其他图片地址"+imageOtherUrl);
        String imageMainUrl = uploadUtil.uploadOneFile(imageMainFile,"D://upload_2021//shopsystem",request,"commodity");
        System.out.println("更新后的主图地址"+imageMainUrl);
        commodity.setComImageOther(imageOtherUrl);
        commodity.setComImageMain(imageMainUrl);
        System.out.println(commodity);
        boolean result = commodityService.addCommodity(commodity);
        if (result){
            return "addComSuccess";
        }else {
            return "addComFail";
        }
    }

    @PostMapping("/findMyCommodityBySellerId")
    public List<Commodity> findMyCommodityBySellerId(Commodity commodity){
        return commodityService.findMyCommodityBySellerId(commodity);
    }

    @PostMapping("/updateComFlagByComId")
    public String updateComFlagByComId(Commodity commodity){
        boolean result = commodityService.withdrawComFlagByComId(commodity);
        if (result){
            return "updateSuccess";
        }else {
            return "updateFail";
        }
    }

    @PostMapping("/updateComPublished")
    @ResponseBody
    public String updateComPublished(Commodity commodity,MultipartFile imageMainFile,MultipartFile[] imageOtherFiles,HttpServletRequest request,
                                String otherImageOriginalUrl,String mainImageOriginalUrl)throws Exception{
        System.out.println(mainImageOriginalUrl);
        System.out.println(otherImageOriginalUrl);
        String[] ortherImageOrigialUrlTran = otherImageOriginalUrl.split(", ");//将String字符串解析为数组
        String imageOtherUrl = uploadUtil.uploadMutiFileUpdate(imageOtherFiles,"D://upload_2021//shopsystem",request,"commodity",ortherImageOrigialUrlTran);
        System.out.println("更新后的其他图片："+imageOtherUrl);
        String imageMainUrl = uploadUtil.uploadOneFileUpdate(imageMainFile,"D://upload_2021//shopsystem",request,"commodity",mainImageOriginalUrl);
        System.out.println("更新后的主图片："+imageMainUrl);
        commodity.setComImageMain(imageMainUrl);
        String newOriginalUrl = "";
        int size = ortherImageOrigialUrlTran.length;
        for(int i=0;i<size;i++){
            if (size==1){
                newOriginalUrl = ortherImageOrigialUrlTran[0].substring(1,ortherImageOrigialUrlTran[i].length()-1);
            }
            else if(0==i){
                newOriginalUrl = ortherImageOrigialUrlTran[0].substring(1);
            }else if(i==size-1){
                newOriginalUrl+=" "+ortherImageOrigialUrlTran[i].substring(0,ortherImageOrigialUrlTran[i].length()-1);
            }else {
                newOriginalUrl+=" "+ortherImageOrigialUrlTran[i];
            }
        }
        if (newOriginalUrl.equals("")){
            commodity.setComImageOther(imageOtherUrl);
        }else {
            commodity.setComImageOther(newOriginalUrl + " " + imageOtherUrl);
        }
        System.out.println(commodity);
        boolean result = commodityService.updateCommodityByComId(commodity);
        if (result){
            return "updateComSuccess";
        }else {
            return "updateComFail";
        }


    }






}

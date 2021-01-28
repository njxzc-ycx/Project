package com.njxzc.shopsystem.controller;

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


@Controller
@RequestMapping("commodity")
public class CommodityController {

    @Autowired
    CommodityService commodityService;

    @RequestMapping("/toAllComByPage")
    public ModelAndView toAllComByPage(String strPageIndex, String strPageSize,ModelAndView mav)throws Exception{
        // 判断并获取页码然后传递给页面
        int pageIndex = 1;
        if(strPageIndex != null && strPageIndex.matches("\\d+")) {
            pageIndex = Integer.parseInt(strPageIndex);
        }
        mav.addObject("pageIndex", pageIndex);

        // 获取并判断每页显示记录数并传递给页面
        int pageSize = 3;
        if(strPageSize != null && strPageSize.matches("\\d+")) {
            pageSize = Integer.parseInt(strPageSize);
        }
        mav.addObject("pageSize", pageSize);

        // 获取表中数据量并判断需要生成多少页，然后传递给页面
        Map<String, Object> parameter = new Hashtable<String, Object>();
        int dataCount = commodityService.findAllComCounts(parameter);
        int pageCount = 0;
        if (dataCount % pageSize == 0) {
            pageCount = dataCount / pageSize;
        } else {
            pageCount = (dataCount / pageSize) + 1;
        }
        mav.addObject("pageCount", pageCount);
        // 根据页数循环遍历页码传递给页面
        List<String> listStringPage = new ArrayList<String>();
        for (int i = 1; i <= pageCount; i++) {
            listStringPage.add(String.valueOf(i));
        }
        mav.addObject("listStringPage", listStringPage);

        // 根据页码和每页显示记录数计算偏移量，并分页查询部门数据
        int startIndex = (pageIndex - 1) * pageSize;
        Map<String, Object> mapParammeter = new HashMap<String, Object>();
        mapParammeter.put("startIndex", startIndex);
        mapParammeter.put("pageSize", pageSize);
        List<Commodity> allCommodities = commodityService.findAllComByPage(mapParammeter);
        System.out.println(allCommodities);
        mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
        mav.setViewName("allCommodity");//设置跳转页面

        return mav;
    }

    @GetMapping("/toComDetail/{comId}")
    public ModelAndView toComDetail(ModelAndView mav,@PathVariable("comId")int comId)throws Exception{
        //1.查询商品信息
        Commodity commodity = commodityService.findCommodityByComId(comId);
        //2.解析其他多个图片
        List<String> urlList = commodityService.dealComImageOther(commodity.getComImageOther());
        mav.addObject("commodity",commodity);
        mav.addObject("urlList",urlList);
        mav.setViewName("comDetail");
        return mav;
    }

    @GetMapping("/toAddCom")
    public String toAddCom()throws Exception{
        return "addCom";
    }

    @PostMapping("/addCom")
    @ResponseBody
    public ModelAndView addCom(MultipartFile pic1,ModelAndView mav, Commodity commodity)throws Exception{
        if(pic1.isEmpty()){
            System.out.println("未获取到文件！");
            mav.addObject("addMsg","发布失败");
        }else {
            String fileName = pic1.getOriginalFilename();
            System.out.println("文件名：" + fileName);
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
            //根据不同后缀 将不同类型文件上传到不同文件夹（前端控制）
            System.out.println("文件后缀：" + suffixName);
            //文件保存地址 最后一定要添加//
            String filePath = "D://upload_2021//shopsystem//image//";
            //文件名重构 考虑到文件同名的问题，这里最好将文件名通过UUID重新处理
            fileName = UUID.randomUUID() + suffixName;//这样可以保证生成的文件名 不重复
            File dest = new File(filePath + fileName);//生成文件地址
            //目录不存在则创建
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }//目录不存在则创建
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            try{
                pic1.transferTo(dest);
                String comImageMain = "http://localhost:8088/shopsystem/resources/image/"+fileName;
                commodity.setComImageMain(comImageMain);
                System.out.println("获得到的Commodity："+commodity);
                boolean result = commodityService.addCommodity(commodity);
                if (result){
                    mav.addObject("addMsg","发布成功");
                }else {
                    mav.addObject("addMsg","发布失败");
                }
            }catch (Exception e){
                System.out.println(e);
                mav.addObject("addMsg","发布失败");
            }
        }
        //1.调用CommodityService中商品发布业务逻辑
        //2.CommodityMapper编写insert方法
        mav.setViewName("addComResult");
        return mav;
    }

    @PostMapping("/addCom2")
    public ModelAndView addCom2(MultipartFile imageMainFile, ModelAndView mav, Commodity commodity, HttpServletRequest request)throws Exception{
        System.out.println("获得到的Commodity："+commodity);
        String imageMainUrl = uploadUtil.uploadOneFile(imageMainFile,"D://upload_2021//shopsystem",request,"commodity");
        commodity.setComImageMain(imageMainUrl);
        //1.调用CommodityService中商品发布业务逻辑
        //2.CommodityMapper编写insert方法
        boolean result = commodityService.addCommodity(commodity);
        if (result){
            mav.addObject("addMsg","发布成功");
        }else {
            mav.addObject("addMsg","发布失败");
        }
        mav.setViewName("addComResult");
        return mav;
    }

    @PostMapping("/addCom3")
    public ModelAndView addCom3(MultipartFile imageMainFile,MultipartFile[] imageOtherFiles,ModelAndView mav, Commodity commodity, HttpServletRequest request)throws Exception{

        String imageMainUrl = uploadUtil.uploadOneFile(imageMainFile,"D://upload_2021//shopsystem",request,"commodity");
        String imageOtherUrl = uploadUtil.uploadMutiFiles(imageOtherFiles,"D://upload_2021//shopsystem",request,"commodity");
        commodity.setComImageMain(imageMainUrl);
        commodity.setComImageOther(imageOtherUrl);
        //1.调用CommodityService中商品发布业务逻辑
        //2.CommodityMapper编写insert方法
        System.out.println("获得到的Commodity："+commodity);
        boolean result = commodityService.addCommodity(commodity);
        if (result){
            mav.addObject("addMsg","发布成功");
        }else {
            mav.addObject("addMsg","发布失败");
        }
        mav.setViewName("addComResult");
        return mav;
    }

    @GetMapping("/deleteCom/{comId}")
    public ModelAndView deleteCom(ModelAndView mav,@PathVariable("comId")int comId)throws Exception{
        //1.根据商品编号 删除商品
        boolean result = commodityService.deleteCommodity(comId);
        if (result){
            System.out.println("删除成功！");
            mav.addObject("deleteInfo","删除成功！");
        }else {
            System.out.println("删除失败！");
            mav.addObject("deleteInfo","删除失败！");
        }
        //2.删除后跳转回商品主页，一定要查询数据
        List<Commodity> allCommodities = commodityService.findAllCommodity();
        List<Commodity> hitsCommodities = commodityService.findCommodityByHits();
        System.out.println(allCommodities);
        System.out.println(hitsCommodities);
        mav.setViewName("commodityIndex");//设置跳转页面
        mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
        mav.addObject("hitsCommodities",hitsCommodities);//将查询到的信息放入Request域中
        return mav;
    }


    @GetMapping("/updateComFlag/{comId}/{flag}")
    public String updateComFlag(ModelAndView mav,@PathVariable("comId")int comId,@PathVariable("flag")int flag)throws Exception{
        Commodity commodity = new Commodity();
        commodity.setComId(comId);//将得到的编号放入Commodity对象
        commodity.setFlag(flag);//将得到的原始状态放入Commodity对象
        System.out.println("Id与原始flag"+commodity.toString());
        //1.调用修改方法
        boolean result = commodityService.updateComFlagByComId(commodity);
        if (result){
            System.out.println("修改成功！");
            mav.addObject("updateFlagInfo","修改成功！");
        }else {
            System.out.println("修改失败！");
            mav.addObject("updateFlagInfo","修改失败！");
        }

        return "redirect:/commodity/toAllComByPage";
    }

    //跳转到商品修改页面 首先要将能够修改的信息 显示 在页面
    @GetMapping("/toUpdateCom/{comId}")
    public ModelAndView toUpdateCom(ModelAndView mav,@PathVariable("comId")int comId)throws Exception{
        //1.查询商品信息
        Commodity commodity = commodityService.findCommodityByComId(comId);
        System.out.println(commodity);
        //2.解析其他多个图片
        List<String> urlList = commodityService.dealComImageOther(commodity.getComImageOther());
        mav.addObject("commodity",commodity);
        mav.addObject("urlList",urlList);
        mav.setViewName("comUpdate");
        return mav;
    }

    @PostMapping("/updateComTest")
    @ResponseBody
    public String updateComTest(Commodity commodity,MultipartFile imageMainFile,MultipartFile[] imageOtherFiles,HttpServletRequest request,
                                String[] otherImageOriginalUrl,String mainImageOriginalUrl)throws Exception{
        System.out.println("前台传递的Commodity："+commodity.toString());
        String imageOtherUrl = uploadUtil.uploadMutiFileUpdate(imageOtherFiles,"D://upload_2021//shopsystem",request,"commodity",otherImageOriginalUrl);
        System.out.println("更新后的其他图片："+imageOtherUrl);
        String imageMainUrl = uploadUtil.uploadOneFileUpdate(imageMainFile,"D://upload_2021//shopsystem",request,"commodity",mainImageOriginalUrl);
        System.out.println("更新后的主图片："+imageMainUrl);
        //将处理好的其他图片地址，主图片地址放入commodity对象
        commodity.setComImageMain(imageMainUrl);
        commodity.setComImageOther(imageOtherUrl);
        boolean result = commodityService.updateComByComId(commodity);
        if (result){
           return "updateSuccess";
        }else {
            return "updateFail";
        }

    }

    //根据关键字查询商品
    @PostMapping("/searchComBykey")
    public ModelAndView searchComBykey(ModelAndView mav,String key)throws Exception{
        List<Commodity> allCommodities = commodityService.findComByKey(key);
        List<Commodity> hitsCommodities = commodityService.findCommodityByHits();
        System.out.println(allCommodities);
        System.out.println(hitsCommodities);
        mav.setViewName("commodityIndex");//设置跳转页面
        mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
        mav.addObject("hitsCommodities",hitsCommodities);//将查询到的信息放入Request域中
        return mav;
    }

}

package com.njxzc.shopsystem.controller;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.User;
import com.njxzc.shopsystem.service.CommodityService;
import com.njxzc.shopsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class TestController {

    @Autowired
    UserService userService;//这里的userService大小写要和 @Service("userService")一致

    @Autowired
    CommodityService commodityService;

    /*当你访问：localhost:8088/toIndex  可以访问到templates下的index.html*/
    @RequestMapping("/toIndex")
    public String toIndex()throws Exception{

        return "index";
    }

    @RequestMapping("/toTest")
    public String toTest()throws Exception{
        return "adminIndex";
    }

    @RequestMapping("/toDefault")
    public String toDefault()throws Exception{
        return "default";
    }

    @RequestMapping("/toCommodity")
    public ModelAndView toCommodity(ModelAndView mav)throws Exception{
        List<Commodity> allCommodities = commodityService.findAllCommodity();
        List<Commodity> hitsCommodities = commodityService.findCommodityByHits();
        System.out.println(allCommodities);
        System.out.println(hitsCommodities);
        mav.setViewName("allCommodity");//设置跳转页面
        mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
        mav.addObject("hitsCommodities",hitsCommodities);//将查询到的信息放入Request域中
        return mav;
    }



    @RequestMapping("/toIndexMav")
    private ModelAndView toIndexMav(ModelAndView mav)throws Exception {
        mav.setViewName("index");
        mav.addObject("name", "殷");//可以是任意内容，例如：对象，字符串，集合等
        mav.addObject("classname", "17软工3班");
        User user = new User(1001,"yinchenxu","123","123","殷晨旭","17软工3班",1,new Date(),1,
            "南京晓庄学院","144156625@qq.com","15195871880","p1.jpg",852.26);
        mav.addObject("user",user);

        //多个对象组成的集合
        User yzh = new User(1002,"yuzhenhao","123","321","於镇皓","17软工4班",0,new Date(),1,
                "南京晓庄学院","144156626@qq.com","15195871888","p2.jpg",800.26);
        User zyj = new User(1003,"zhanyujie","123","花折人心碎","张玉杰","17软工4班",1,new Date(),1,
                "南京晓庄学院","144156636@qq.com","15195871088","p3.jpg",860.26);
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(yzh);
        users.add(zyj);
        mav.addObject("users",users);
        return mav;
    }

    /*文件上传 单文件*/
    @PostMapping("/upload1")
    @ResponseBody
    public String upload1(MultipartFile pic1,User user,String userName)throws Exception{
        System.out.println("获取到的User："+ user.toString());
        System.out.println("获取到的userName"+userName);
        if(pic1.isEmpty()){
            System.out.println("未获取到文件！");
            return "fileMissing!";
        }else {
            String fileName = pic1.getOriginalFilename();
            System.out.println("文件名："+fileName);
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
            //根据不同后缀 将不同类型文件上传到不同文件夹（前端控制）
            System.out.println("文件后缀："+suffixName);
            //文件保存地址 最后一定要添加//
            String filePath = "D://upload_2021//shopsystem//image//";
            //文件名重构 考虑到文件同名的问题，这里最好将文件名通过UUID重新处理
            fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
            File dest = new File(filePath+fileName);//生成文件地址
            //目录不存在则创建
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            try{
                pic1.transferTo(dest);
                return "uploadSuccess";
            }catch (Exception e){
                System.out.println(e);
                return "uploadFail";
            }
        }
    }
    /*多文件上传*/
    @PostMapping("/upload2")
    @ResponseBody
    public String upload2(MultipartFile[] files)throws  Exception{
        System.out.println("获取到的文件数量："+files.length);
        for(int i=0;i<files.length;i++){
            if(files[i].isEmpty()){
                System.out.println("未获取到文件！位置："+i);
            }else {
                String fileName = files[i].getOriginalFilename();
                System.out.println("文件名："+fileName);
                String suffixName = fileName.substring(fileName.lastIndexOf("."));//获取文件后缀
                //根据不同后缀 将不同类型文件上传到不同文件夹（前端控制）
                System.out.println("文件后缀："+suffixName);
                //文件保存地址 最后一定要添加//
                String filePath = "D://upload_2021//shopsystem//image//";
                fileName = UUID.randomUUID()+suffixName;//这样可以保证生成的文件名 不重复
                File dest = new File(filePath+fileName);//生成文件地址
                //目录不存在则创建
                if(!dest.getParentFile().exists()){
                    dest.getParentFile().mkdirs();
                }try{
                    files[i].transferTo(dest);
                }catch (Exception e){
                    System.out.println(e);
                    return "uploadFail";
                }
            }
        }
        return "uploadSuccess";

    }

    //不登录也能调到商品主页
    @GetMapping("/toCommodityIndex")
    public ModelAndView toCommodityIndex(ModelAndView mav)throws Exception{
        Commodity commodity = new Commodity();
        commodity.setFlag(1);//我们设计的商品状态为1 是上架商品
        List<Commodity> allCommodities = commodityService.findAllComByCondition(commodity);
        List<Commodity> hitsCommodities = commodityService.findCommodityByHits();
        System.out.println(allCommodities);
        System.out.println(hitsCommodities);
        mav.setViewName("commodityIndex");//设置跳转页面
        mav.addObject("allCommodities",allCommodities);//将查询到的信息放入Request域中
        mav.addObject("hitsCommodities",hitsCommodities);//将查询到的信息放入Request域中
        return mav;
    }

}

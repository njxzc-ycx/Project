package com.njxzc.shopsystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import com.njxzc.shopsystem.service.TypeService;
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
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("type")
public class TypeController {

    @Autowired
    TypeService typeService;

    @RequestMapping("/toAllTypes")
    public String toAllTypes()throws Exception{
        return "allTypes";
    }

    @RequestMapping("/toTypesecond")
    public ModelAndView toTypesecond(ModelAndView mav,int firstId)throws Exception{
        mav.addObject("firstId",firstId);
        mav.setViewName("typeseconds");
        return mav;
    }

    @RequestMapping("/toTypethird")
    public ModelAndView toTypethird(ModelAndView mav,int secondId)throws Exception{
        mav.addObject("secondId",secondId);
        mav.setViewName("typethirds");
        return mav;
    }

    @RequestMapping(value ="/findAllTypefirst")
    @ResponseBody
    public Map<String,Object> findAllTypefirst(@RequestParam(required=false,defaultValue="1") int page,
                                               @RequestParam(required=false) int limit) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Typefirst> allTypefirst  = typeService.findAllTypefirst();
        System.out.println(allTypefirst);
        // 使用pageInfo包装查询
        PageInfo<Typefirst> rolePageInfo = new PageInfo<>(allTypefirst);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @RequestMapping(value ="/findAllTypesecondByFirstId")
    @ResponseBody
    public Map<String,Object> findAllTypesecondByFirstId(@RequestParam(required=false,defaultValue="1") int page,
                                               @RequestParam(required=false) int limit,int firstId) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Typesecond> typeseconds  = typeService.findAllTypesecondByFirstId(firstId);
        // 使用pageInfo包装查询
        PageInfo<Typesecond> rolePageInfo = new PageInfo<>(typeseconds);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @RequestMapping(value ="/findAllTypethirdBySecondId")
    @ResponseBody
    public Map<String,Object> findAllTypethirdBySecondId(@RequestParam(required=false,defaultValue="1") int page,
                                               @RequestParam(required=false) int limit,int secondId) {
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<Typethird> typethirds  = typeService.findAllTypethirdBySecondId(secondId);
        // 使用pageInfo包装查询
        PageInfo<Typethird> rolePageInfo = new PageInfo<>(typethirds);//*/
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",rolePageInfo.getTotal());
        map.put("data",rolePageInfo.getList());
        return map;
    }

    @PostMapping("/addTypefirst")
    @ResponseBody
    public void addTypefirst(Typefirst typefirst){
        typeService.addTypefirst(typefirst);
    }

    @PostMapping("/deleteTypefirst")
    @ResponseBody
    public void deleteTypefirst(Typefirst typefirst){
        typeService.deleteTypefirst(typefirst);
    }

    @PostMapping("/updateTypefirst")
    @ResponseBody
    public void updateTypefirst(Typefirst typefirst){
        typeService.updateTypefirst(typefirst);
    }

    @PostMapping("/updateTypesecond")
    @ResponseBody
    public void updateTypesecond(Typesecond typesecond){
        typeService.updateTypesecond(typesecond);
    }

    @PostMapping("/addTypesecond")
    @ResponseBody
    public void addTypesecond(Typesecond typesecond){
        typeService.addTypesecond(typesecond);
    }

    @PostMapping("/deleteTypesecond")
    @ResponseBody
    public void deleteTypesecond(Typesecond typesecond){
        typeService.deleteTypesecond(typesecond);
    }

    @ResponseBody
    @RequestMapping("/addTypethird")
    public JSON addTypethird(MultipartFile file,Typethird typethird ,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        String thirdPicture = uploadUtil.uploadOneFile(file,"D://upload_2021//shopsystem",request,"typethird");
        System.out.println(thirdPicture);
        typethird.setThirdPicture(thirdPicture);
        boolean result = typeService.addTypethird(typethird);
        if (result){
            json.put("code",0);
            json.put("file",thirdPicture);
        }else {
            json.put("code",1);
        }

        return json;

    }

    @PostMapping("/deleteTypethird")
    @ResponseBody
    public void deleteTypethird(Typethird typethird){
        typeService.deleteTypethird(typethird);
    }

    @ResponseBody
    @RequestMapping("/updateTypethird")
    public JSON updateTypethird(MultipartFile file,Typethird typethird ,HttpServletRequest request) {
        JSONObject json=new JSONObject();
        String thirdPicture = uploadUtil.uploadOneFile(file,"D://upload_2021//shopsystem",request,"typethird");
        System.out.println(thirdPicture);
        typethird.setThirdPicture(thirdPicture);
        boolean result = typeService.updateTypethird(typethird);
        if (result){
            json.put("code",0);
            json.put("file",thirdPicture);
        }else {
            json.put("code",1);
        }

        return json;

    }


}

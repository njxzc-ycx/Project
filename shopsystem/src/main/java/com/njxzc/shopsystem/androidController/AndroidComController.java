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

    @PostMapping("/addTest")
    @ResponseBody
    public void addTest(Commodity commodity){
        commodityService.addCommodity(commodity);
    }



}

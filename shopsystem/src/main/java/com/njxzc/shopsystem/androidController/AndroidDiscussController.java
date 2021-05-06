package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Commodity;
import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.DiscussType;
import com.njxzc.shopsystem.service.DiscussService;
import com.njxzc.shopsystem.utils.uploadUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("androidDiscuss")
public class AndroidDiscussController {

    @Resource
    DiscussService discussService;

    @PostMapping("/findDiscussByUserId")
    public List<Discuss> findDiscussByUserId(Discuss discuss){
        return discussService.findDiscussByUserId(discuss);
    }

    @PostMapping("/findMyDiscussByUserId")
    public List<Discuss> findMyDiscussByUserId(Discuss discuss){
        return discussService.findMyDiscussByUserId(discuss);
    }

    @PostMapping("/findDiscussByType")
    public List<Discuss> findDiscussByType(Discuss discuss){
        return discussService.findDiscussByType(discuss);
    }

    @GetMapping("/findAllDiscuss")
    public List<Discuss> findAllDiscuss(){
        return discussService.findAllDiscuss();
    }

    @GetMapping("/findAllDiscussType")
    public List<DiscussType> findAllDiscussType(){
        return discussService.findAllDiscussType();
    }

    @PostMapping("/addDiscuss")
    public String addDiscuss(@RequestParam("discussImagesFiles") MultipartFile[] discussImagesFiles, Discuss discuss,
                         HttpServletRequest request)throws Exception{
        System.out.println(discussImagesFiles);
        String discussImages = uploadUtil.uploadMutiFiles(discussImagesFiles,"D://upload_2021//shopsystem",request,"discuss");
        System.out.println("更新后的其他图片地址"+discussImages);
        discuss.setDiscussImages(discussImages);
        System.out.println(discuss);
        boolean result = discussService.addDiscuss(discuss);
        if (result){
            return "addSuccess";
        }else {
            return "addFail";
        }
    }

    @PostMapping("/deleteDisByDiscussId")
    @ResponseBody
    public String deleteDisByDiscussId(Discuss discuss)throws Exception{
        boolean check = discussService.deleteDiscuss(discuss);
        if (check){
            return "deleteSuccess";
        }else {
            return "deleteFail";
        }
    }

    @PostMapping("/addDisscussUp")
    public void addDisscussUp(Discuss discuss){
        discussService.addDisscussUp(discuss);
    }

    @PostMapping("/addDisscussHits")
    public void addDisscussHits(Discuss discuss){
        discussService.addDisscussHits(discuss);
    }

    @PostMapping("/findDiscussByDiscussId")
    public Discuss findDiscussByDiscussId(Discuss discuss){
        return discussService.findDiscussByDiscussId(discuss);
    }
}

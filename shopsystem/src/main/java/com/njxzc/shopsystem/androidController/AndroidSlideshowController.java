package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Slideshow;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import com.njxzc.shopsystem.service.SlideshowService;
import com.njxzc.shopsystem.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("androidSlideshow")
public class AndroidSlideshowController {

    @Autowired
    SlideshowService slideshowService;

    @GetMapping("/findSlideshows")
    private List<Slideshow> findSlideshows()throws Exception{
        return  slideshowService.findSlideshows();

    }
}

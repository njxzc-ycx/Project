package com.njxzc.shopsystem.service;


import com.njxzc.shopsystem.pojo.Slideshow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlideshowService {

    //查询显示的轮播图
    List<Slideshow> findSlideshows();

    //查询所有轮播图
    List<Slideshow> findAllSlideshows();

    //更改轮播图状态
    boolean updateSlideshowStatus(int slideshowId,int slideshowStatus);

    //查询轮播图显示个数
    int findSlideshowedCount();

    //删除轮播图
    boolean deleteSlideshow(int slideshowId);

    //更新轮播图图片
    boolean updateSlideshow(int slideshowId,String slideshowUrl);

    //添加轮播图
    boolean addSlideshow(@Param("slideshowUrl") String slideshowUrl);
}

package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.Slideshow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SlideshowMapper {

    //查询显示的轮播图
    List<Slideshow> findSlideshows();

    //查询所有轮播图
    List<Slideshow> findAllSlideshows();

    //更改轮播图状态
    int updateSlideshowStatus(@Param("slideshowId") int slideshowId,@Param("slideshowStatus") int slideshowStatus);

    //查询轮播图显示个数
    int findSlideshowedCount();

    //删除轮播图
    int deleteSlideshow(@Param("slideshowId") int slideshowId);

    //更新轮播图图片
    int updateSlideshow(@Param("slideshowId") int slideshowId,@Param("slideshowUrl") String slideshowUrl);

    //添加轮播图
    int addSlideshow(@Param("slideshowUrl") String slideshowUrl);



}

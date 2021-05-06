package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.SlideshowMapper;
import com.njxzc.shopsystem.mapper.TypeMapper;
import com.njxzc.shopsystem.pojo.Slideshow;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("SlideshowService")
public class SlideshowServiceImpl implements SlideshowService {

    @Resource
    SlideshowMapper slideshowMapper;

    @Override
    public List<Slideshow> findSlideshows() {
        return slideshowMapper.findSlideshows();
    }

    @Override
    public List<Slideshow> findAllSlideshows() {
        return slideshowMapper.findAllSlideshows();
    }

    @Override
    public boolean updateSlideshowStatus(int slideshowId, int slideshowStatus) {
         if (1==slideshowStatus){
            slideshowStatus=2;
        }else if (2==slideshowStatus){
            slideshowStatus=1;
        }
        try {
            int result = slideshowMapper.updateSlideshowStatus(slideshowId,slideshowStatus);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public int findSlideshowedCount() {
        return slideshowMapper.findSlideshowedCount();
    }

    @Override
    public boolean deleteSlideshow(int slideshowId) {
        try {
            int result = slideshowMapper.deleteSlideshow(slideshowId);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateSlideshow(int slideshowId, String slideshowUrl) {
        try {
            int result = slideshowMapper.updateSlideshow(slideshowId,slideshowUrl);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addSlideshow(String slideshowUrl) {
        try {
            int result = slideshowMapper.addSlideshow(slideshowUrl);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}

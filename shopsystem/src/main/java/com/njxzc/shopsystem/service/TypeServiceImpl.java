package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.OrderDetailMapper;
import com.njxzc.shopsystem.mapper.TypeMapper;
import com.njxzc.shopsystem.pojo.OrderDetail;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("TypeService")
public class TypeServiceImpl implements TypeService {

    @Resource
    TypeMapper typeMapper;


    @Override
    public List<Typefirst> findAllTypefirst() {
        return typeMapper.findAllTypefirst();
    }

    @Override
    public List<Typesecond> findAllTypesecondByFirstId(int firstId) {
        return typeMapper.findAllTypesecondByFirstId(firstId);
    }

    @Override
    public List<Typethird> findAllTypethird(int firstId) {
        return typeMapper.findAllTypethird(firstId);
    }

    @Override
    public List<Typethird> findAllTypethirdBySecondId(int secondId) {
        return typeMapper.findAllTypethirdBySecondId(secondId);
    }

    @Override
    public boolean addTypefirst(Typefirst typefirst) {
        try {
            int result = typeMapper.addTypefirst(typefirst);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteTypefirst(Typefirst typefirst) {
        try {
            int result = typeMapper.deleteTypefirst(typefirst);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateTypefirst(Typefirst typefirst) {
        try {
            int result = typeMapper.updateTypefirst(typefirst);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addTypesecond(Typesecond typesecond) {
        try {
            int result = typeMapper.addTypesecond(typesecond);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteTypesecond(Typesecond typesecond) {
        try {
            int result = typeMapper.deleteTypesecond(typesecond);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateTypesecond(Typesecond typesecond) {
        try {
            int result = typeMapper.updateTypesecond(typesecond);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean addTypethird(Typethird typethird) {
        try {
            int result = typeMapper.addTypethird(typethird);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean deleteTypethird(Typethird typethird) {
        try {
            int result = typeMapper.deleteTypethird(typethird);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public boolean updateTypethird(Typethird typethird) {
        try {
            int result = typeMapper.updateTypethird(typethird);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }
}

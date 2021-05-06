package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.OrderDetail;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeService {

    //查询所有一级分类
    List<Typefirst> findAllTypefirst();

    //根据一级分类编号查询二级分类
    List<Typesecond> findAllTypesecondByFirstId(int firstId);

    //查询三级分类
    List<Typethird> findAllTypethird(int firstId);

    //根据二级分类编号查询三级分类
    List<Typethird> findAllTypethirdBySecondId(int secondId);

    //添加一级分类
    boolean addTypefirst(Typefirst typefirst);

    //删除一级分类
    boolean deleteTypefirst(Typefirst typefirst);

    //更新一级分类
    boolean updateTypefirst(Typefirst typefirst);

    //添加二级分类
    boolean addTypesecond(Typesecond typesecond);

    //删除二级分类
    boolean deleteTypesecond(Typesecond typesecond);

    //更新二级分类
    boolean updateTypesecond(Typesecond typesecond);

    //添加三级分类
    boolean addTypethird(Typethird typethird);

    //删除三级分类
    boolean deleteTypethird(Typethird typethird);

    //更新三级分类
    boolean updateTypethird(Typethird typethird);
}


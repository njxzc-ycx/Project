package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeMapper {

    //查询所有一级分类
    List<Typefirst> findAllTypefirst();

    //根据一级分类编号查询二级分类
    List<Typesecond> findAllTypesecondByFirstId(@Param("firstId") int firstId);

    //根据二级分类编号查询三级分类
    List<Typethird> findAllTypethirdBySecondId(@Param("secondId") int secondId);

    //查询三级分类
    List<Typethird> findAllTypethird(@Param("firstId") int firstId);

    //添加一级分类
    int addTypefirst(Typefirst typefirst);

    //删除一级分类
    int deleteTypefirst(Typefirst typefirst);

    //更新一级分类
    int updateTypefirst(Typefirst typefirst);

    //添加二级分类
    int addTypesecond(Typesecond typesecond);

    //删除二级分类
    int deleteTypesecond(Typesecond typesecond);

    //更新二级分类
    int updateTypesecond(Typesecond typesecond);

    //添加三级分类
    int addTypethird(Typethird typethird);

    //删除三级分类
    int deleteTypethird(Typethird typethird);

    //更新三级分类
    int updateTypethird(Typethird typethird);

}

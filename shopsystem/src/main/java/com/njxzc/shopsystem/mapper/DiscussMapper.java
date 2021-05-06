package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.DiscussType;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussMapper {

    //根据用户编号查询帖子(不包括匿名)
    List<Discuss> findDiscussByUserId(Discuss discuss);

    //根据用户编号查询帖子
    List<Discuss> findMyDiscussByUserId(Discuss discuss);

    //根据帖子编号查询帖子
    Discuss findDiscussByDiscussId(Discuss discuss);

    //查询所有帖子
    List<Discuss> findAllDiscuss();

    //查询分类的帖子
    List<Discuss> findDiscussByType(Discuss discuss);

    //查询帖子的分类
    List<DiscussType> findAllDiscussType();

    //发布帖子
    int addDiscuss(Discuss discuss);

    //删除帖子
    int deleteDiscuss(Discuss discuss);

    //点赞
    int addDisscussUp(Discuss discuss);

    //增加评论数
    int addDisscussComments(Discuss discuss);

    //增加访问数
    int addDisscussHits(Discuss discuss);

    //查询帖子数
    int findDiscussCount();

    //管理员查询所有帖子
    List<Discuss> findAllDiscusses();

    //删除帖子
    int updateDiscussFlag(@Param("discussId") int discussId,@Param("flag") int flag);

    //关键字查询帖子
    List<Discuss> findDiscussesByKey(@Param("flag") int flag,@Param("typeId") int typeId,
                                     @Param("discussId") int discussId,@Param("nickName") String nickName,@Param("key") String key);
}

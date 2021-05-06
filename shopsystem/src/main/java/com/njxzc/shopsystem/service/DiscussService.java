package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Discuss;
import com.njxzc.shopsystem.pojo.DiscussType;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussService {

    //根据用户编号查询讨论
    List<Discuss> findDiscussByUserId(Discuss discuss);

    //根据用户编号查询讨论
    List<Discuss> findMyDiscussByUserId(Discuss discuss);

    //根据讨论编号查询讨论
    Discuss findDiscussByDiscussId(Discuss discuss);

    //查询所有讨论
    List<Discuss> findAllDiscuss();

    //查询分类的讨论
    List<Discuss> findDiscussByType(Discuss discuss);

    //查询讨论的分类
    List<DiscussType> findAllDiscussType();

    //发布讨论
    boolean addDiscuss(Discuss discuss);

    //删除帖子
    boolean deleteDiscuss(Discuss discuss);

    //点赞
    boolean addDisscussUp(Discuss discuss);

    //增加评论数
    boolean addDisscussComments(Discuss discuss);

    //增加访问数
    boolean addDisscussHits(Discuss discuss);

    //查询帖子数
    int findDiscussCount();

    //管理员查询所有讨论
    List<Discuss> findAllDiscusses();

    //删除帖子
    boolean updateDiscussFlag(int discussId,int flag);

    //关键字查询帖子
    List<Discuss> findDiscussesByKey(int flag,int typeId,int discussId,String nickName,String key);

}

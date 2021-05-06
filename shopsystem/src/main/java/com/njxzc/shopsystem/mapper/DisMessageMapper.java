package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.DisMessage;
import com.njxzc.shopsystem.pojo.DisReplay;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DisMessageMapper {

    //根据编号查询留言
    List<DisMessage> findDisMsgByDiscussId(DisMessage disMessage);

    //根据编号和编号 查询 回复留言
    List<DisReplay> findDisReplayByDiscussIdAndMsgId(@Param("discussId") int discussId, @Param("msgId") int msgId);

    //新增评论
    int addDisMessage(DisMessage disMessage);

    //新增回复
    int addDisReplay(DisReplay disReplay);

    //查询帖子评论数
    int findDisMessageCounts();

    int findDisReplayCounts();

    
}

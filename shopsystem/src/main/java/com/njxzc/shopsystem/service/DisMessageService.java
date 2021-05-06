package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.DisMessage;
import com.njxzc.shopsystem.pojo.DisReplay;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DisMessageService {

    //根据编号查询留言
    List<DisMessage> findDisMsgByDiscussId(DisMessage disMessage);

    //根据编号和编号 查询 回复留言
    List<DisReplay> findDisReplayByDiscussIdAndMsgId(int discussId,int msgId);

    //新增评论
    boolean addDisMessage(DisMessage disMessage);

    //新增回复
    boolean addDisReplay(DisReplay disReplay);

    //查询帖子评论数
    int findDisMessageCounts();

    int findDisReplayCounts();
}

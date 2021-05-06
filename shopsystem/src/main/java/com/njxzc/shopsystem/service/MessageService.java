package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageService {

    //根据商品编号查询商品留言
    List<Message> findMsgByComId(Message message);

    //根据商品编号和留言编号 查询 回复留言
    List<Replay> findReplayByComIdAndMsgId(int comId,int msgId);

    //新增评论
    boolean addMessage(Message message);

    //新增评论回复
    boolean addReplay(Replay replay);

}

package com.njxzc.shopsystem.mapper;


import com.njxzc.shopsystem.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {

    //根据商品编号查询商品留言
    List<Message> findMsgByComId(Message message);

    //根据商品编号和留言编号 查询 回复留言
    List<Replay> findReplayByComIdAndMsgId(@Param("comId") int comId, @Param("msgId") int msgId);

    //新增商品评论
    int addMessage(Message message);

    //新增商品评论回复
    int addReplay(Replay replay);


}

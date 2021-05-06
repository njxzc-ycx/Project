package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.MessageMapper;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    MessageMapper messageMapper;

    @Override
    public List<Message> findMsgByComId(Message message) {
        return messageMapper.findMsgByComId(message);
    }

    @Override
    public List<Replay> findReplayByComIdAndMsgId(int comId, int msgId) {
        return messageMapper.findReplayByComIdAndMsgId(comId,msgId);
    }

    @Override
    public boolean addMessage(Message message) {
        try {
            int result = messageMapper.addMessage(message);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }

    @Override
    public boolean addReplay(Replay replay) {
        try {
            int result = messageMapper.addReplay(replay);
            if(result>0){
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return false;
    }
}

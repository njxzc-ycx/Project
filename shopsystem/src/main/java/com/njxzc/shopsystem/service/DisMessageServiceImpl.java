package com.njxzc.shopsystem.service;

import com.njxzc.shopsystem.mapper.DisMessageMapper;
import com.njxzc.shopsystem.mapper.MessageMapper;
import com.njxzc.shopsystem.pojo.DisMessage;
import com.njxzc.shopsystem.pojo.DisReplay;
import com.njxzc.shopsystem.pojo.Message;
import com.njxzc.shopsystem.pojo.Replay;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional
@Service("disMessageService")
public class DisMessageServiceImpl implements DisMessageService {

    @Resource
    DisMessageMapper disMessageMapper;

    @Override
    public List<DisMessage> findDisMsgByDiscussId(DisMessage disMessage) {
        return disMessageMapper.findDisMsgByDiscussId(disMessage);
    }

    @Override
    public List<DisReplay> findDisReplayByDiscussIdAndMsgId(int discussId, int msgId) {
        return disMessageMapper.findDisReplayByDiscussIdAndMsgId(discussId,msgId);
    }

    @Override
    public boolean addDisMessage(DisMessage disMessage) {
        try {
            int result = disMessageMapper.addDisMessage(disMessage);
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
    public boolean addDisReplay(DisReplay disReplay) {
        try {
            int result = disMessageMapper.addDisReplay(disReplay);
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
    public int findDisMessageCounts() {
        return disMessageMapper.findDisMessageCounts();
    }

    @Override
    public int findDisReplayCounts() {
        return disMessageMapper.findDisReplayCounts();
    }
}

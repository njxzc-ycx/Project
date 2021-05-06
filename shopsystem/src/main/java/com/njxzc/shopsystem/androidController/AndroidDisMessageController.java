package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.*;
import com.njxzc.shopsystem.service.DisMessageService;
import com.njxzc.shopsystem.service.DiscussService;
import com.njxzc.shopsystem.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("androidDisMsg")
public class AndroidDisMessageController {

    @Autowired
    DisMessageService disMessageService;

    @Autowired
    DiscussService discussService;

    @PostMapping("/findDisMsgByDiscussId")
    public List<DisMessage> findDisMsgByDiscussId(DisMessage disMessage){
        return disMessageService.findDisMsgByDiscussId(disMessage);
    }

    @PostMapping("/findDisReplayByDiscussIdAndMsgId")
    public List<DisReplay> findDisReplayByDiscussIdAndMsgId(int discussId, int msgId){
        return disMessageService.findDisReplayByDiscussIdAndMsgId(discussId,msgId);
    }

    @PostMapping("/addDisMessage")
    public List<DisMessage> addDisMessage(DisMessage disMessage)throws Exception{
        boolean check = disMessageService.addDisMessage(disMessage);
        if (check){
            Discuss discuss = new Discuss();
            int discussId = disMessage.getDiscussId();
            discuss.setDiscussId(discussId);
            discussService.addDisscussComments(discuss);
            return findDisMsgAndDisReplayBydiscussId(disMessage);
        }else {
            return null;
        }

    }

    @PostMapping("/addDisReplay")
    public List<DisMessage> addDisReplay(DisReplay disReplay,int discussId)throws Exception{
        DisMessage disMessage = new DisMessage();
        disMessage.setDiscussId(discussId);
        boolean check = disMessageService.addDisReplay(disReplay);
        if (check){
            disMessage.setDiscussId(discussId);
            Discuss discuss = new Discuss();
            discuss.setDiscussId(discussId);
            discussService.addDisscussComments(discuss);
            return findDisMsgAndDisReplayBydiscussId(disMessage);
        }else {
            return null;
        }

    }

    @PostMapping("/findDisMsgAndDisReplayBydiscussId")
    public List<DisMessage> findDisMsgAndDisReplayBydiscussId(DisMessage disMessage){
        List<DisMessage> dismessages = disMessageService.findDisMsgByDiscussId(disMessage);
        System.out.println(dismessages);
        ArrayList arrayList = new ArrayList();
        for (int i=0;i<dismessages.size();i++) {
            ArrayList arrayList2 = new ArrayList();
            int discussId = dismessages.get(i).getDiscussId();
            int msgId = dismessages.get(i).getMsgId();
            List<DisReplay> disreplays = disMessageService.findDisReplayByDiscussIdAndMsgId(discussId,msgId);
            for (int k=0;k<disreplays.size();k++) {
                arrayList2.add(disreplays.get(k));
            }
            Map map = new HashMap();
            map.put("disreplay",arrayList2);
            map.put("dismessage",dismessages.get(i));
            arrayList.add(map);
        }

        return arrayList;
    }



}

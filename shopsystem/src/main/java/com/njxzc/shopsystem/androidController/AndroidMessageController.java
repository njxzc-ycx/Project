package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.*;
import com.njxzc.shopsystem.service.MessageService;
import com.njxzc.shopsystem.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("androidMsg")
public class AndroidMessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/findMsgByComId")
    public List<Message> findMsgByComId(Message message){
        return messageService.findMsgByComId(message);
    }

    @PostMapping("/findReplayByComIdAndMsgId")
    public List<Replay> findReplayByComIdAndMsgId(int comId, int msgId){
        return messageService.findReplayByComIdAndMsgId(comId,msgId);
    }

    @PostMapping("/addMessage")
    public List<Message> addMessage(Message message)throws Exception{
        boolean check = messageService.addMessage(message);
        if (check){
            return findMsgAndReplayByComId(message);
        }else {
            return null;
        }

    }

    @PostMapping("/addReplay")
    public List<Message> addReplay(Replay replay,int comId)throws Exception{
        Message message = new Message();
        message.setComId(comId);
        boolean check = messageService.addReplay(replay);
        if (check){
            message.setComId(comId);
            return findMsgAndReplayByComId(message);
        }else {
            return null;
        }

    }

    @PostMapping("/findMsgAndReplayByComId")
    public List<Message> findMsgAndReplayByComId(Message message){
        List<Message> messages = messageService.findMsgByComId(message);
        ArrayList arrayList = new ArrayList();

        for (int i=0;i<messages.size();i++) {
            ArrayList arrayList2 = new ArrayList();
            int comId = messages.get(i).getComId();
            int msgId = messages.get(i).getMsgId();
            List<Replay> replays = messageService.findReplayByComIdAndMsgId(comId,msgId);
            for (int k=0;k<replays.size();k++) {
                arrayList2.add(replays.get(k));
            }
            Map map = new HashMap();
            map.put("replay",arrayList2);
            map.put("message",messages.get(i));
            arrayList.add(map);
        }

        return arrayList;
    }



}

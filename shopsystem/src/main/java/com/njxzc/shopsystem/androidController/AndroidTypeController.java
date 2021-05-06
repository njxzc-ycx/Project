package com.njxzc.shopsystem.androidController;

import com.njxzc.shopsystem.pojo.Collect;
import com.njxzc.shopsystem.pojo.Typefirst;
import com.njxzc.shopsystem.pojo.Typesecond;
import com.njxzc.shopsystem.pojo.Typethird;
import com.njxzc.shopsystem.service.CollectService;
import com.njxzc.shopsystem.service.CommodityService;
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
@RequestMapping("androidType")
public class AndroidTypeController {

    @Autowired
    TypeService typeService;

    @GetMapping("/findAllTypefirst")
    public List<Typefirst> findAllTypefirst(){
        return typeService.findAllTypefirst();
    }

    @PostMapping("/findAllTypesecondByFirstId")
    public List<Typesecond> findAllTypesecondByFirstId(int firstId){
        List<Typesecond> typeseconds = typeService.findAllTypesecondByFirstId(firstId);
        ArrayList arrayList = new ArrayList();
        for (int i=0;i<typeseconds.size();i++){
            ArrayList arrayList2 = new ArrayList();
            int secondId = typeseconds.get(i).getSecondId();
            List<Typethird> typethirds = typeService.findAllTypethirdBySecondId(secondId);
            for (int k=0;k<typethirds.size();k++) {
                arrayList2.add(typethirds.get(k));
            }
            Map map = new HashMap();
            map.put("typethird",arrayList2);
            map.put("typesecond",typeseconds.get(i));
            arrayList.add(map);
        }
        return arrayList;
    }

    @PostMapping("/findAllTypethirdBySecondId")
    public List<Typethird> findAllTypethirdBySecondId(int secondId){
        return typeService.findAllTypethirdBySecondId(secondId);

    }@PostMapping("/findAllTypethird")
    public List<Typethird> findAllTypethird(int firstId){
        return typeService.findAllTypethird(firstId);
    }


}

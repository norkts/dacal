package com.norkts.dacal.dacal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.collect.Lists;
import com.norkts.dacal.domain.GiftType;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempTest {
    @Test
    public void genJson(){
        Map<String,Integer> giftMap = new HashMap<>();
        {
            giftMap.put("糖果摩天轮", 2);
            giftMap.put("云端之梦", 5);
            giftMap.put("他趣嘉年华", 10);
            giftMap.put("世纪婚礼", 100);

            giftMap.put("烂漫云端", 1);
            giftMap.put("梦幻独角兽", 2);
            giftMap.put("做我的猫", 10);
            giftMap.put("我们的爱", 10);

            giftMap.put("少女星愿", 3);
            giftMap.put("时空之门", 10);
            giftMap.put("流星光遇", 50);
            giftMap.put("远洋之旅", 5);

        }

        List<GiftType> jsons = Lists.newArrayList();

        giftMap.forEach((k,v) -> {
            jsons.add(GiftType.builder()
                            .id(jsons.size() + 1)
                            .giftName(k)
                            .value(v*100)
                    .build());
        });

        try {
            System.out.println((new JsonMapper()).writeValueAsString(jsons));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

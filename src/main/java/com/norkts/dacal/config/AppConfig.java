package com.norkts.dacal.config;

import com.google.common.collect.Maps;
import com.norkts.dacal.db.dao.CommonMapper;
import com.norkts.dacal.domain.Config;
import com.norkts.dacal.domain.GamblingData;
import com.norkts.dacal.domain.GiftType;
import com.norkts.dacal.util.CommonUtil;
import com.norkts.dacal.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.InflaterInputStream;

@Configuration
public class AppConfig {

    @Resource
    private CommonMapper commonMapper;

    @Bean("giftTypeMap")
    public Map<String, GiftType> giftTypeMap() {

        Map<String, GiftType> giftTypeMap = Maps.newConcurrentMap();

        List<Map<String,Object>> rows = commonMapper.queryDataByWhere("GiftType", "1=1", 0L, 200L);

        if(CollectionUtils.isEmpty(rows)){
            return giftTypeMap;
        }

        rows.forEach(row -> {

            GiftType giftType = CommonUtil.dbMap2Object(row, new GiftType());

            giftTypeMap.put(giftType.getGiftName(), CommonUtil.dbMap2Object(row, giftType));
        });

        return giftTypeMap;
    }

    @Bean("gamblingData")
    public GamblingData gamblingData(){
        List<Map<String,Object>> rows = commonMapper.queryDataByWhere("Config", "key='GamblingData'", 0L, 200L);

        if(CollectionUtils.isEmpty(rows)){
            return new GamblingData();
        }

        Config config = CommonUtil.dbMap2Object(rows.get(0), new Config());

        GamblingData gamblingData = CommonUtil.pasreObject(config.getValue(), GamblingData.class);

        if(gamblingData == null){
            gamblingData = new GamblingData();
        }

        return gamblingData;
    }
}

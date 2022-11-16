package com.norkts.dacal.job;

import com.norkts.dacal.db.dao.CommonMapper;
import com.norkts.dacal.domain.Config;
import com.norkts.dacal.domain.GamblingData;
import com.norkts.dacal.types.Constants;
import com.norkts.dacal.util.CommonUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GiftDataJob {
    private ScheduledExecutorService poolExecutor = Executors.newScheduledThreadPool(5);

    @Resource
    private GamblingData gamblingData;

    @Resource
    private CommonMapper commonMapper;

    @PostConstruct
    public void init(){
        poolExecutor.scheduleAtFixedRate(() -> {

            String data = CommonUtil.toBinString(gamblingData);
            if(data == null){
                return;
            }

            Config config = Config.builder()
                    .key(Constants.GAMBLING_DATA_KEY)
                    .value(data)
                    .gmtCreate(new Date())
                    .gmtModified(new Date())
                    .build();
            commonMapper.insert("Config", CommonUtil.object2DbMap(config));
        }, 1, 1, TimeUnit.SECONDS);
    }
}

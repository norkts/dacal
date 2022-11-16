package com.norkts.dacal.controller;

import com.google.common.collect.Maps;
import com.norkts.dacal.domain.*;
import com.norkts.dacal.domain.params.request.MessageDTO;
import com.norkts.dacal.domain.params.request.RawMsgData;
import com.norkts.dacal.domain.params.response.GiftNotice;
import com.norkts.dacal.domain.params.response.ResultDTO;
import com.norkts.dacal.helper.parser.IGiftParser;
import com.norkts.dacal.types.GiftSceneEnum;
import com.norkts.dacal.types.PlatformEnum;
import com.norkts.dacal.util.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import com.norkts.dacal.db.dao.CommonMapper;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TaQuController {

    @Resource
    private GamblingData gamblingData;

    @Resource
    private IGiftParser giftParser;

    @Resource
    private Map<String, GiftType> giftTypeMap;

    @Resource
    private CommonMapper commonMapper;

    @RequestMapping(value = "/msg/gift", method = RequestMethod.POST)
    public ResultDTO<GiftNotice> recieveGiftMessage(@RequestBody List<MessageDTO> msgs){

        for(MessageDTO messageDTO : msgs){

            commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                            .platform(PlatformEnum.TAQU.getCode())
                            .msg(messageDTO.getMsg())
                            .scenne(messageDTO.getType())
                            .time(messageDTO.getTime())
                    .build()));

            GiftMessage giftMessage = giftParser.parse(messageDTO);

            commonMapper.insert("GiftMessage", CommonUtil.object2DbMap(giftMessage));

            GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());

            if(giftType == null){
                continue;
            }

            dealYLC(giftMessage);

            dealMWX(giftMessage);
            dealTWX(giftMessage);
            dealBJX(giftMessage);

            dealHLMP(giftMessage);
            dealZZMP(giftMessage);

            dealMH(giftMessage);
        }

        return ResultDTO.<GiftNotice>builder()
                .success(true)
                .data(GiftNotice.builder()
                        .oriGamblingData(gamblingData)
                        .rollSummaryText(gamblingData.rollSummary.getSummary())
                        .rollSummaryTexts(gamblingData.rollSummary.getSummaryHistorys().getItems())
                        .roll2Count(gamblingData.rollSummary.getG2CountText())
                        .roll2TimeText(gamblingData.rollSummary.getLastGiftTime())
                        .planetSummaryText(gamblingData.planetSummary.getSummary())
                        .yuanYangTimeText(gamblingData.cardSummary.getYuanYangTime())
                        .yuanYangPeriodText(gamblingData.cardSummary.getYuanYangPeriod())
                        .yuanYangPeriodTexts(gamblingData.cardSummary.getYuanYangSummaryHistorys().getItems())
                        .bigCardSumText(gamblingData.cardSummary.getBigCardSummary())
                        .bigCardSumTexts(gamblingData.cardSummary.getBigCardSummaryHistorys().getItems())
                        .moheSummaryText(gamblingData.moheSummary.getSummary())
                        .moheSummaryTexts(gamblingData.moheSummary.getMeheSummaryHistory().getItems())
                        .build())
                .build();
    }

    /**
     * 游乐场
     * @param giftMessage
     */
    public void dealYLC(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.YLC.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 200){
                gamblingData.rollSummary.onG2();
            }

            if(giftType.getValue() == 500){
                gamblingData.rollSummary.onG5();
            }

            if(giftType.getValue() == 1000){
                gamblingData.rollSummary.onG10();
            }

            if(giftType.getValue() == 10000){
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.rollSummary.getSummary())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("游乐场-100.0")
                        .build()));
                gamblingData.rollSummary.onG100();
            }
        }
    }

    /**
     * 冥王星
     * @param giftMessage
     */
    public void dealMWX(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.MWX.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 1000){
                gamblingData.planetSummary.onG10("m");
            }
        }
    }

    /**
     * 天王星
     * @param giftMessage
     */
    public void dealTWX(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.TWX.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 1000){
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.planetSummary.getSummary())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("天王星-10.0")
                        .build()));
                gamblingData.planetSummary.onG10("t");
            }

            if(giftType.getValue() == 200){
                gamblingData.planetSummary.onG2();
            }
        }
    }

    /**
     * 北极星
     * @param giftMessage
     */
    public void dealBJX(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.BJX.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 1000){
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.planetSummary.getSummary())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("北极星-10.0")
                        .build()));
                gamblingData.planetSummary.onG10("b");
            }

            if(giftType.getValue() == 100){
                gamblingData.planetSummary.onG1();
            }
        }
    }

    /**
     * 至尊魔牌
     * @param giftMessage
     */
    public void dealZZMP(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.ZZMP.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 334){
                gamblingData.cardSummary.onG3();
            }

            if(giftType.getValue() == 1000){
                gamblingData.cardSummary.onG10();
            }

            if(giftType.getValue() == 5000){
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.cardSummary.getBigCardSummary())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("至尊魔牌-50.0")
                        .build()));
                gamblingData.cardSummary.onG50();
            }
        }
    }

    /**
     * 欢乐魔牌
     * @param giftMessage
     */
    public void dealHLMP(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(GiftSceneEnum.HLMP.getDesc().equals(giftMessage.getScene())){
            if(giftType.getValue() == 500){
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.cardSummary.getYuanYangPeriod())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("欢乐魔牌-5.0时间间隔")
                        .build()));
                gamblingData.cardSummary.onG5();
            }
        }
    }

    /**
     * 魔盒
     * @param giftMessage
     */
    public void dealMH(GiftMessage giftMessage){
        GiftType giftType = giftTypeMap.get(giftMessage.getGiftName());
        if(giftMessage.getScene().contains(GiftSceneEnum.MH.getDesc())){

            if(giftType.getValue() == 20){
                gamblingData.moheSummary.onG02();
            }

            if(giftType.getValue() == 50){
                gamblingData.moheSummary.onG05();
            }

            if(giftType.getValue() == 100){
                gamblingData.moheSummary.onG1();
            }

            if(giftType.getValue() == 500){
                gamblingData.moheSummary.onG5();
                commonMapper.insert("RawMessage", CommonUtil.object2DbMap(RawMsgData.builder()
                        .time(giftMessage.getTime())
                        .msg(gamblingData.moheSummary.getSummary())
                        .platform(PlatformEnum.TAQU.getCode())
                        .scenne("魔盒5.0")
                        .build()));
            }
        }
    }


    @RequestMapping(value = "/gift/result")
    public ResultDTO<GiftNotice> getGiftNotice(){
        return ResultDTO.<GiftNotice>builder()
                .success(true)
                .data(GiftNotice.builder()
                        .oriGamblingData(gamblingData)
                        .rollSummaryText(gamblingData.rollSummary.getSummary())
                        .rollSummaryTexts(gamblingData.rollSummary.getSummaryHistorys().getItems())
                        .roll2Count(gamblingData.rollSummary.getG2CountText())
                        .roll2TimeText(gamblingData.rollSummary.getLastGiftTime())
                        .planetSummaryText(gamblingData.planetSummary.getSummary())
                        .yuanYangTimeText(gamblingData.cardSummary.getYuanYangTime())
                        .yuanYangPeriodText(gamblingData.cardSummary.getYuanYangPeriod())
                        .yuanYangPeriodTexts(gamblingData.cardSummary.getYuanYangSummaryHistorys().getItems())
                        .bigCardSumText(gamblingData.cardSummary.getBigCardSummary())
                        .bigCardSumTexts(gamblingData.cardSummary.getBigCardSummaryHistorys().getItems())
                        .build())
                .build();
    }


    @RequestMapping(value = "/gift/clear", method = RequestMethod.GET)
    public ResultDTO<Void> clear(@Param("type") String type){

        if(Objects.equals(type, "YLC")){
            gamblingData.rollSummary.clear();
        }

        if(Objects.equals(type, "XJMH")){
            gamblingData.planetSummary.clear();
        }

        if(Objects.equals(type, "MP")){
            gamblingData.cardSummary.clear();
        }

        return ResultDTO.<Void>builder().success(true).build();
    }

    @RequestMapping(value = "/gift/type/edit", method = RequestMethod.POST)
    public ResultDTO<Void> giftEdit(@RequestBody List<GiftType> giftTypes){

        for(GiftType giftType : giftTypes){
            giftTypeMap.put(giftType.getGiftName(), giftType);

            commonMapper.insert("GiftType", CommonUtil.object2DbMap(giftType));
        }

        return ResultDTO.<Void>builder().success(true).build();
    }

    @RequestMapping(value = "/db/query", method = RequestMethod.GET)
    public ResultDTO<List<Map<String, Object>>> dbQuery(@RequestParam("table") String table
            , @RequestParam("where") String where
            , @RequestParam("start") Long start
            , @RequestParam("limit") Long limit){

        List<Map<String, Object>> rows = commonMapper.queryDataByWhere(table, Optional.ofNullable(where).orElse("1=1"), Optional.ofNullable(start).orElse(0L)
                , Optional.ofNullable(limit).orElse(1000L));

        return ResultDTO.<List<Map<String, Object>>>builder().success(true).data(rows).build();
    }

    @RequestMapping(value = "/db/execute", method = RequestMethod.POST)
    public ResultDTO<Object> dbExecute(@RequestBody String sql){

        Object result;
        if(sql.toLowerCase().startsWith("select")){
            result = commonMapper.select(sql);
        }else{
            result = commonMapper.execute(sql);
        }


        return ResultDTO.builder().success(true).data(result).build();
    }
}

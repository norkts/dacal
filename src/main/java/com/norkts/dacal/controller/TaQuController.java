package com.norkts.dacal.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.norkts.dacal.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class TaQuController {

    private List<GiftMessage> giftMessages = Lists.newArrayList();

    private Date lastG5 = null;
    private Date lastG2 = null;
    private Date lastG10 = null;
    private Date lastG100 = null;

    private AtomicInteger g2NumAfterLastG5 = new AtomicInteger(0);
    private AtomicInteger g2NumAfterLastG10 = new AtomicInteger(0);
    private AtomicInteger g5NumAfterLastG10 = new AtomicInteger(0);
    private AtomicInteger g10NumAfterLastG100 = new AtomicInteger(0);
    private GfCounter g100Counter = new GfCounter();


    private String g2Name = "糖果摩天轮";

    private String g5Name = "云端之梦";

    private String g10Name = "他趣嘉年华";

    private String g100Name = "世纪婚礼";

    @RequestMapping(value = "/msg/gift", method = RequestMethod.POST)
    public ResultDTO<GiftNotice> recieveGiftMessage(@RequestBody List<MessageDTO> msgs){
        List<GiftMessage> curGifts = convertGifts(msgs);

        curGifts.stream().sorted().forEach((msg) -> {
            if(Objects.equals(g2Name, msg.getGiftName())){

                //2分钟以上可能有2.0
                //4分钟以上可能有8.0
                lastG2 = msg.getTime();

                g100Counter.getG2Counter().addAndGet(msg.getNum());

                //超过4个可能有2.0
                //超过8个可能有10.0
                int num =  g2NumAfterLastG5.addAndGet(msg.getNum());

                g2NumAfterLastG10.addAndGet(msg.getNum());
            }

            if(Objects.equals(g5Name, msg.getGiftName())){
                lastG5 = msg.getTime();
                g2NumAfterLastG5.set(0);
                g5NumAfterLastG10.addAndGet(msg.getNum());
                //超过4个可能有10.0
                int num =  g100Counter.getG5Counter().addAndGet(msg.getNum());
            }

            if(Objects.equals(g10Name, msg.getGiftName())){
                lastG10 = msg.getTime();

                g2NumAfterLastG10.set(0);
                g5NumAfterLastG10.set(0);
                g10NumAfterLastG100.addAndGet(msg.getNum());
                g100Counter.getG10Counter().addAndGet(msg.getNum());
            }

            if(Objects.equals(g100Name, msg.getGiftName())){
                lastG100 = msg.getTime();
                g100Counter.clearG2510();

                g100Counter.getG100Counter().addAndGet(msg.getNum());
            }
        });

        return ResultDTO.<GiftNotice>builder()
                .success(true)
                .data(GiftNotice.builder()
                        .lastG5Date(lastG5)
                        .lastG2Date(lastG2)
                        .lastG10Date(lastG10)
                        .lastG100Date(lastG100)
                        .g2NumAfterLastG5(g2NumAfterLastG5.get())
                        .g2NumAfterLastG10(g2NumAfterLastG10.get())
                        .g5NumAfterLastG10(g5NumAfterLastG10.get())
                        .g10NumAfterLastG100(g10NumAfterLastG100.get())
                        .g100Sumary(g100Counter.toString())
                        .build())
                .build();
    }

    @RequestMapping(value = "/gift/summary")
    public ResultDTO<GiftNotice> getGiftNotice(){
        return ResultDTO.<GiftNotice>builder()
                .success(true)
                .data(GiftNotice.builder()
                        .lastG5Date(lastG5)
                        .lastG2Date(lastG2)
                        .lastG10Date(lastG10)
                        .lastG100Date(lastG100)
                        .g2NumAfterLastG5(g2NumAfterLastG5.get())
                        .g2NumAfterLastG10(g2NumAfterLastG10.get())
                        .g5NumAfterLastG10(g5NumAfterLastG10.get())
                        .g10NumAfterLastG100(g10NumAfterLastG100.get())
                        .g100Sumary(g100Counter.toString())
                        .build())
                .build();
    }

    public static Pattern LE_CHANG_REG_EXP = Pattern.compile("^(.+)\\s+在(.+)中获得\\s+(.+?)[,，].*$");

    public static List<GiftMessage> convertGifts(List<MessageDTO> msgs){

        List<GiftMessage> gifts = new ArrayList<>();
        for(MessageDTO nodeInfo : msgs){
            if(nodeInfo.getMsg() != null){
                String msg = nodeInfo.getMsg();
                //寻*** 在游乐场中获得 糖果摩天轮，恭喜！我也要玩>>
                Matcher matcher = LE_CHANG_REG_EXP.matcher(msg);
                if(matcher.matches()){
                    GiftMessage giftMessage = new GiftMessage();
                    giftMessage.setUser(matcher.group(1));
                    giftMessage.setGiftType(matcher.group(2));

                    String[] splits = matcher.group(3).split("x");
                    giftMessage.setGiftName(splits[0]);
                    if(splits.length > 1){
                        giftMessage.setNum(Integer.parseInt(splits[1]));
                    }else{
                        giftMessage.setNum(1);
                    }


                    giftMessage.setTime(nodeInfo.getTime());
                    giftMessage.setNo(nodeInfo.getSn());

                    //TODO 判断重复
                    gifts.add(giftMessage);
                }
            }
        }

        return gifts;
    }

    @RequestMapping(value = "/gift/clear", method = RequestMethod.GET)
    public ResultDTO<Void> clear(){
        lastG2 = null;
        lastG5 = null;
        lastG10 = null;
        lastG100 = null;

        g2NumAfterLastG5.set(0);
        g2NumAfterLastG10.set(0);
        g5NumAfterLastG10.set(0);
        g10NumAfterLastG100.set(0);

        g100Counter.clearAll();
        return ResultDTO.<Void>builder().success(true).build();
    }
}

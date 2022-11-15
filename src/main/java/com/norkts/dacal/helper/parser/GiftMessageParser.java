package com.norkts.dacal.helper.parser;

import com.google.common.collect.Lists;
import com.norkts.dacal.domain.GiftMessage;
import com.norkts.dacal.domain.GiftType;
import com.norkts.dacal.domain.params.request.MessageDTO;
import com.norkts.dacal.types.PlatformEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GiftMessageParser implements IGiftParser{
    public static Pattern LE_CHANG_REG_EXP = Pattern.compile("^(.+)\\s在(.+)中获得\\s(.+?)，.*$");

    public static Pattern PLANET_REG_EXP = Pattern.compile("^(.+)在(.+)寻宝获得(.+)$");

    public static Pattern CARD_REG_EXP = Pattern.compile("^(.+)在(.+)中获得(.+?),.*$");

    private List<IGiftParser> parsers = Lists.newArrayList(new MoheMessageParser());

    @Override
    public GiftMessage parse(MessageDTO messageDTO) {

        String text = messageDTO.getMsg();

        Matcher matcher = LE_CHANG_REG_EXP.matcher(text);
        if(!matcher.find()){
            matcher = PLANET_REG_EXP.matcher(text);

            if(!matcher.find()){

                matcher = CARD_REG_EXP.matcher(text);

                if(!matcher.find()){
                    for(IGiftParser parser : parsers){
                        return parser.parse(messageDTO);
                    }
                }
            }
        }

        String user = matcher.group(1);
        String scene = matcher.group(2);
        String gift = matcher.group(3);
        String[] splits = gift.split("x");

        return GiftMessage.builder()
                .user(user)
                .scene(scene)
                .giftName(splits[0].trim())
                .num(splits.length > 1 ? Integer.parseInt(splits[1].trim()) : 1)
                .platform(PlatformEnum.TAQU.getCode())
                .time(messageDTO.getTime())
                .build();
    }
}

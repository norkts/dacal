package com.norkts.dacal.helper.parser;

import com.norkts.dacal.domain.GiftMessage;
import com.norkts.dacal.domain.params.request.MessageDTO;
import com.norkts.dacal.types.PlatformEnum;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoheMessageParser implements IGiftParser{

    private static final Pattern MOHE_REG_EXP = Pattern.compile("^(.*)赠送(.*)(\\d+)个(.*?),.*开出(.*)$");
    @Override
    public GiftMessage parse(MessageDTO messageDTO) {
        String text = messageDTO.getMsg();

        Matcher matcher = MOHE_REG_EXP.matcher(text);
        if(!matcher.find()){
            return null;
        }

        String user = matcher.group(1);

        String scene = matcher.group(4);
        String gift = matcher.group(5);
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

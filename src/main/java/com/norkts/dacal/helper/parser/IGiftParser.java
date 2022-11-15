package com.norkts.dacal.helper.parser;

import com.norkts.dacal.domain.GiftMessage;
import com.norkts.dacal.domain.GiftType;
import com.norkts.dacal.domain.params.request.MessageDTO;

public interface IGiftParser {
    GiftMessage parse(MessageDTO messageDTO);
}

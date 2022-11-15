package com.norkts.dacal.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class PlatformEnum {
    public static final PlatformEnum TAQU = new PlatformEnum(1,"他趣");

    private int code;
    private String name;
    PlatformEnum(int code, String name){
        this.code = code;
        this.name = name;
    }
}

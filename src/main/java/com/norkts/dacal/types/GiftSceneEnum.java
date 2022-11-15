package com.norkts.dacal.types;

import java.util.Objects;

public enum GiftSceneEnum {
    YLC(0, "游乐场"),
    MWX(11, "冥王星"),
    TWX(11, "天王星"),
    BJX(11, "北王星"),
    HLMP(21, "欢乐魔牌"),
    ZZMP(22, "至尊魔牌"),
    MH(4, "魔盒"),
    YGBH(5, "月光宝盒");

    private int code;
    private String desc;

    GiftSceneEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static GiftSceneEnum getByCode(int code){
        for (GiftSceneEnum sceneTypeEnum : values()){
            if(Objects.equals(sceneTypeEnum.code, code)){
                return sceneTypeEnum;
            }
        }

        return null;
    }
}

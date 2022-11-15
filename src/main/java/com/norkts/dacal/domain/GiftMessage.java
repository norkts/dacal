package com.norkts.dacal.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftMessage implements Serializable {

    private static final long serialVersionUID = -5108163803959638573L;

    private Integer id;

    private String user;

    private Date time;

    private String scene;

    private String giftName;

    private Integer num;

    private int platform;

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  format.format(time) + " " + user + " 在" + scene + "中获得了" + giftName + "x" + num;
    }

    public boolean equals(GiftMessage giftMessage){

        if(giftMessage == this){
            return true;
        }

        return Objects.equals(user, giftMessage.user)
                && Objects.equals(time, giftMessage.time)
                && Objects.equals(scene, giftMessage.scene)
                && Objects.equals(giftName, giftMessage.giftName);
    }
}

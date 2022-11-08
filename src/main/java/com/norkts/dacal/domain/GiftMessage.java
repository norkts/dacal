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
    private String no;

    private String user;

    private Date time;

    private String giftType;

    private String giftName;

    private Integer num;

    @Override
    public String toString(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  format.format(time) + " " + user + " 在" + giftType + "中获得了" + giftName + "x" + num;
    }

    @Override
    public int hashCode(){
        return no.hashCode();
    }

    public boolean equals(GiftMessage giftMessage){

        if(giftMessage == this){
            return true;
        }

        return Objects.equals(no, giftMessage.no)
                && Objects.equals(user, giftMessage.user)
                && Objects.equals(time, giftMessage.time)
                && Objects.equals(giftType, giftMessage.giftType)
                && Objects.equals(giftName, giftMessage.giftName);
    }
}

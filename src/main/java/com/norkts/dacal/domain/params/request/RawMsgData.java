package com.norkts.dacal.domain.params.request;

import com.norkts.dacal.types.PlatformEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMsgData implements Serializable {
    private static final long serialVersionUID = 697298957777026956L;

    private Integer id;

    private Date time;

    private String msg;

    private String scenne;

    /**
     * @see PlatformEnum
     */
    private int platform;
}

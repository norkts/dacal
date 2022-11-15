package com.norkts.dacal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftType implements Serializable {
    private static final long serialVersionUID = -4190774039762534573L;
    private Integer id;
    private Integer value;
    private String giftName;
}

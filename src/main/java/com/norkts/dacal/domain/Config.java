package com.norkts.dacal.domain;

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
public class Config implements Serializable {
    private static final long serialVersionUID = -8960562107783883177L;
    private String key;
    private String value;
    private Date gmtCreate;
    private Date gmtModified;
}

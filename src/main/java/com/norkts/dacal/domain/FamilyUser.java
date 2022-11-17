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
public class FamilyUser implements Serializable {
    private static final long serialVersionUID = 2361150063438277812L;
    private Integer id;

    private String userNick;

    private String familyName;

    private Integer familyId;

    private Integer age;

    private Integer weiwang;

    private Integer wealth;

    private Integer meili;

    private Integer talkVal;

    private Integer lastestTime;
}

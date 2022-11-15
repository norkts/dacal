package com.norkts.dacal.domain.params.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultDTO<T> implements Serializable {
    private static final long serialVersionUID = -3080846186294120508L;

    private T data;

    private boolean success;

    private String errorCode;

    private String errorMessage;
}

package com.ssafy.mozip.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    INVALID_REQUEST(1000, "유효하지 않은 요청입니다");

    private final int code;
    private final String message;
}

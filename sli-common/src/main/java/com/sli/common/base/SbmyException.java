package com.sli.common.base;


import com.sli.common.assertion.AssertionResultCode;

public class SbmyException extends BaseException {

    public SbmyException(AssertionResultCode errorCodeEnum) {
        super(errorCodeEnum);
    }

    public SbmyException(AssertionResultCode errorCodeEnum, String message) {
        super(errorCodeEnum, message);
    }

    public SbmyException(AssertionResultCode code, String errorMessage, Throwable cause) {
        super(code, errorMessage, cause);
    }

}

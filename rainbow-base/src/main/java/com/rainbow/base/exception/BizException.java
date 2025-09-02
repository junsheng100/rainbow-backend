package com.rainbow.base.exception;

import com.rainbow.base.constant.HttpCode;

public class BizException extends BaseException{


    public BizException(HttpCode httpCode) {
        super(httpCode);
    }


    public BizException(String message) {
        this(null, null, null, message);
    }

    public BizException(String code,String message) {
        this(null, code, null, message);
    }

    public BizException(String module, String code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }
}

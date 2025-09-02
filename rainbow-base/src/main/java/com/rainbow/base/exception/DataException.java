package com.rainbow.base.exception;

import com.rainbow.base.constant.HttpCode;

public class DataException extends BaseException{


    public DataException(HttpCode httpCode) {
        super(httpCode);
    }

    public DataException(String message) {
        this(null, null, null, message);
    }

    public DataException(String module, String code, Object[] args, String defaultMessage) {
        super(module, code, args, defaultMessage);
    }
}

package com.flong.springboot.core.exception;

/**
 * 系统异常
 */
public class FatalException extends BaseException {

    public FatalException(String message) {
        this(CommMsgCode.SYSTEM_ERROR, message);
    }

    public FatalException(String message, Throwable cause) {
        this(CommMsgCode.SYSTEM_ERROR, message, cause);
    }

    public FatalException(MsgCode code) {
        super(code);
    }

    public FatalException(MsgCode code, String message) {
        super(code, message);
    }

    public FatalException(MsgCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public FatalException(MsgCode code, Throwable cause) {
        super(code, cause);
    }

    public FatalException(MsgCode code, String message, Throwable cause,
                          boolean enableSuppression,
                          boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}

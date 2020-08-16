package com.flong.springboot.core.exception;


public class BaseException extends RuntimeException {

    private final MsgCode msgCode;


    public BaseException(MsgCode msgCode) {
        this.msgCode = msgCode;
    }

    public BaseException(String message) {
        super(message);
        this.msgCode = CommMsgCode.SERVICE_ERROR;
    }

    public BaseException(MsgCode msgCode, String message) {
        super(message);
        this.msgCode = msgCode;
    }

    public BaseException(MsgCode msgCode, String message, Throwable cause) {
        super(message, cause);
        this.msgCode = msgCode;
    }

    public BaseException(MsgCode msgCode, Throwable cause) {
        super(cause);
        this.msgCode = msgCode;
    }

    public BaseException(MsgCode msgCode, String message, Throwable cause,
                         boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msgCode = msgCode;
    }

    public MsgCode getMsgCode() {
        return msgCode;
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null || msg.trim().length() == 0) {
            msg = msgCode.getMessage();
        }
        return msg;
    }
}

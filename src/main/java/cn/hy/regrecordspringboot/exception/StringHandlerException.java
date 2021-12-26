package cn.hy.regrecordspringboot.exception;

public class StringHandlerException extends Exception{
    public StringHandlerException() {
        super();
    }

    public StringHandlerException(String message) {
        super(message);
    }

    public StringHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringHandlerException(Throwable cause) {
        super(cause);
    }

    protected StringHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

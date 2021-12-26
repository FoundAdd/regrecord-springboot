package cn.hy.regrecordspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class InsertRecordException extends RuntimeException{
    public InsertRecordException() {
        super();
    }

    public InsertRecordException(String message) {
        super(message);
    }

    public InsertRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public InsertRecordException(Throwable cause) {
        super(cause);
    }

    protected InsertRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

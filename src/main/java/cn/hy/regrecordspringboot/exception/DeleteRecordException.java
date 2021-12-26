package cn.hy.regrecordspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DeleteRecordException extends RuntimeException{
    public DeleteRecordException() {
        super();
    }

    public DeleteRecordException(String message) {
        super(message);
    }

    public DeleteRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteRecordException(Throwable cause) {
        super(cause);
    }

    protected DeleteRecordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

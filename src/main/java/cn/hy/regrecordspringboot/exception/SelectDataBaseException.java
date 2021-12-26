package cn.hy.regrecordspringboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class SelectDataBaseException extends RuntimeException{
    public SelectDataBaseException() {
        super();
    }

    public SelectDataBaseException(String message) {
        super(message);
    }

    public SelectDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SelectDataBaseException(Throwable cause) {
        super(cause);
    }

    protected SelectDataBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

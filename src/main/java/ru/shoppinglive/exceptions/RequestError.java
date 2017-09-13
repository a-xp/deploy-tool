package ru.shoppinglive.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by rkhabibullin on 08.09.2017.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestError extends RuntimeException {
    public RequestError(String message) {
        super(message);
    }

    public RequestError(String message, Throwable cause) {
        super(message, cause);
    }
}

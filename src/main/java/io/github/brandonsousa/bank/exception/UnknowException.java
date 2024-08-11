package io.github.brandonsousa.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnknowException extends RuntimeException {
    public UnknowException(String message) {
        super(message);
    }
}

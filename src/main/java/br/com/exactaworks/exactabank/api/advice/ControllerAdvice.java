package br.com.exactaworks.exactabank.api.advice;

import br.com.exactaworks.exactabank.api.response.ErrorResponse;
import br.com.exactaworks.exactabank.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice {
    private ErrorResponse INTERNAL_SERVER_ERROR_RESPONSE = new ErrorResponse("Internal Server Error");

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse response = new ErrorResponse(
                errors
        );

        log.error(String.valueOf(response));

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("{} {}", INTERNAL_SERVER_ERROR_RESPONSE.traceId(), ex.getMessage());
        return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR_RESPONSE);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("{} {}", INTERNAL_SERVER_ERROR_RESPONSE.traceId(), ex.getMessage());
        return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR_RESPONSE);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        log.error("{} {}", INTERNAL_SERVER_ERROR_RESPONSE.traceId(), ex.getMessage());
        return ResponseEntity.internalServerError().body(INTERNAL_SERVER_ERROR_RESPONSE);
    }
}

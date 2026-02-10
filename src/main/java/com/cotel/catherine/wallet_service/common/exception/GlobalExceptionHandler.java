package com.cotel.catherine.wallet_service.common.exception;

import com.cotel.catherine.wallet_service.exception.WalletErrorCodeEnum;
import com.cotel.catherine.wallet_service.exception.WalletException;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception) {

        if (exception.getCause() instanceof JsonParseException) {
            JsonParseException jsonEx = (JsonParseException) exception.getCause();
            log.error("Invalid JSON. Error at line {}, column {}",
                    jsonEx.getLocation().getLineNr(),
                    jsonEx.getLocation().getColumnNr());
        } else {
            log.error("Invalid JSON format: {}", exception.getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.description(),
                WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.errorCode(),
                WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.status());

        return ResponseEntity.status(errorResponse.status()).body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        log.error("Method argument type mismatch: {}", exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                WalletErrorCodeEnum.NOT_FOUND_WALLET_BY_ID.description(),
                WalletErrorCodeEnum.NOT_FOUND_WALLET_BY_ID.errorCode(),
                WalletErrorCodeEnum.NOT_FOUND_WALLET_BY_ID.status());

        return ResponseEntity.status(errorResponse.status()).body(errorResponse);

    }

    @ExceptionHandler(WalletException.class)
    protected ResponseEntity<ErrorResponse> handleCommunicationException(WalletException exception) {

        log.error("handleCommunicationException -> code={}, ", exception.errorCode(), exception);
        ErrorResponse errorResponse = new ErrorResponse(exception.description(), exception.errorCode(), exception.status());

        return ResponseEntity.status(errorResponse.status()).body(errorResponse);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        log.error("handleMethodArgumentNotValidException -> ", exception);
        ErrorResponse errorResponse = new ErrorResponse()
                .description(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.description())
                .errorCode(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.errorCode())
                .status(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.status());

        return ResponseEntity.status(errorResponse.status()).body(errorResponse);

    }

    @ExceptionHandler(ValidationException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {

        log.error("handleValidationException -> ", exception);
        ErrorResponse errorResponse = new ErrorResponse()
                .description(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.description())
                .errorCode(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.errorCode())
                .status(WalletErrorCodeEnum.WRONG_OBJECT_PARAMS.status());

        return ResponseEntity
                .status(errorResponse.status())
                .body(errorResponse);

    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {

        if (exception instanceof MethodArgumentTypeMismatchException ex) {
            return handleMethodArgumentTypeMismatch(ex);
        }

        log.error("handleException -> ", exception);
        ErrorResponse errorResponse = new ErrorResponse(
                WalletErrorCodeEnum.INTERNAL_SERVER_ERROR.description(),
                WalletErrorCodeEnum.INTERNAL_SERVER_ERROR.errorCode(),
                WalletErrorCodeEnum.INTERNAL_SERVER_ERROR.status());

        return ResponseEntity.status(errorResponse.status()).body(errorResponse);

    }

}

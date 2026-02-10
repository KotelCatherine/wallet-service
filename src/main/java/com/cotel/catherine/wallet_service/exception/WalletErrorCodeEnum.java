package com.cotel.catherine.wallet_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum WalletErrorCodeEnum {

    INTERNAL_SERVER_ERROR("00", "Ошибка сервиса управления данными", HttpStatus.INTERNAL_SERVER_ERROR),
    WRONG_OBJECT_PARAMS("01", "Невалидный json", HttpStatus.BAD_REQUEST),
    NOT_FOUND_WALLET_BY_ID("02", "Такой кошелек не существует", HttpStatus.NOT_FOUND),
    INSUFFICIENT_FUNDS("03", "Недостаточно средств", HttpStatus.BAD_REQUEST),
    CONCURRENT_MODIFICATION("04", "Обновились данные", HttpStatus.CONFLICT);

    private final String errorCode;
    private final String description;
    private final HttpStatus status;

}

package com.cotel.catherine.wallet_service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(fluent = true)
public class WalletException extends Exception{

    private final String errorCode;
    private final String description;
    private final HttpStatus status;

    public WalletException(WalletErrorCodeEnum code) {
        super(code.description());
        this.errorCode = code.errorCode();
        this.description = code.description();
        this.status = code.status();
    }

}

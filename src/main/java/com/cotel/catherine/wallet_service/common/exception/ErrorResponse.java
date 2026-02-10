package com.cotel.catherine.wallet_service.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class ErrorResponse {

    @Schema(description = "Описание ошибки")
    @JsonProperty("description")
    private String description;

    @Schema(description = "Код ошибки")
    @JsonProperty("errorCode")
    private String errorCode;

    @Schema(description = "Статус ошибки")
    @JsonProperty("status")
    private HttpStatus status;

}
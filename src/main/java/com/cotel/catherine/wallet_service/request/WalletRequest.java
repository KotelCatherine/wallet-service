package com.cotel.catherine.wallet_service.request;

import com.cotel.catherine.wallet_service.enums.OperationType;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Запрос на изменение счета в кошельке")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class WalletRequest {

    @Schema(description = "Идентификатор кошелька")
    @JsonProperty("walletId")
    @NotNull
    private UUID walletId;

    @Schema(description = "Тип операции")
    @JsonProperty("operationType")
    @NotNull
    private OperationType operationType;

    @Schema(description = "Сумма")
    @JsonProperty("amount")
    @Positive
    @NotNull
    private BigDecimal amount;

}

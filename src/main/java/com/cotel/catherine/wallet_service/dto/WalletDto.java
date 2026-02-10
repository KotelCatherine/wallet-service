package com.cotel.catherine.wallet_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Данные о кошельке")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class WalletDto {

    @Schema(description = "Идентификатор кошелька")
    @JsonProperty("walletId")
    private UUID walletId;

    @Schema(description = "Сумма")
    @JsonProperty("amount")
    private BigDecimal amount;

}

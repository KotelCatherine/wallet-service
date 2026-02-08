package com.cotel.catherine.wallet_service.resource;

import com.cotel.catherine.wallet_service.dto.WalletDto;
import com.cotel.catherine.wallet_service.exception.WalletException;
import com.cotel.catherine.wallet_service.request.WalletRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Wallet", description = "Кошелек")
public interface WalletResource {

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.CREATED)
    WalletDto updateWallet(@Valid @RequestBody WalletRequest wallet) throws WalletException;

    @GetMapping("/wallets/{WALLET_UUID}")
    @ResponseStatus(HttpStatus.OK)
    WalletDto getWalletById(@PathVariable UUID WALLET_UUID) throws WalletException;

}

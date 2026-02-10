package com.cotel.catherine.wallet_service.controller;

import com.cotel.catherine.wallet_service.dto.WalletDto;
import com.cotel.catherine.wallet_service.exception.WalletException;
import com.cotel.catherine.wallet_service.request.WalletRequest;
import com.cotel.catherine.wallet_service.resource.WalletResource;
import com.cotel.catherine.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletController implements WalletResource {

    private final WalletService service;

    @Override
    @PostMapping("/wallet")
    public WalletDto updateWallet(@Valid @RequestBody WalletRequest wallet) throws WalletException {
        return service.updateWallet(wallet);
    }

    @Override
    @GetMapping("/wallets/{WALLET_UUID}")
    public WalletDto getWalletById(@PathVariable UUID WALLET_UUID) throws WalletException {
        return service.getWalletById(WALLET_UUID);
    }

}

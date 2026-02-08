package com.cotel.catherine.wallet_service.service;

import com.cotel.catherine.wallet_service.dto.WalletDto;
import com.cotel.catherine.wallet_service.exception.WalletException;
import com.cotel.catherine.wallet_service.request.WalletRequest;

import java.util.UUID;

public interface WalletService {

    WalletDto updateWallet(WalletRequest walletRequest) throws WalletException;
    WalletDto getWalletById(UUID WALLET_UUID) throws WalletException;

}

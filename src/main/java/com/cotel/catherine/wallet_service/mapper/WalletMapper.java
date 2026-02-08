package com.cotel.catherine.wallet_service.mapper;

import com.cotel.catherine.wallet_service.dto.WalletDto;
import com.cotel.catherine.wallet_service.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletDto mapToDto(Wallet wallet) {
        return new WalletDto()
                .walletId(wallet.walletId())
                .amount(wallet.amount());
    }

}

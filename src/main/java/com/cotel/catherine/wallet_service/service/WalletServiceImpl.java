package com.cotel.catherine.wallet_service.service;

import com.cotel.catherine.wallet_service.dto.WalletDto;
import com.cotel.catherine.wallet_service.entity.Wallet;
import com.cotel.catherine.wallet_service.enums.OperationType;
import com.cotel.catherine.wallet_service.exception.WalletErrorCodeEnum;
import com.cotel.catherine.wallet_service.exception.WalletException;
import com.cotel.catherine.wallet_service.mapper.WalletMapper;
import com.cotel.catherine.wallet_service.repository.WalletRepository;
import com.cotel.catherine.wallet_service.request.WalletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WalletDto updateWallet(WalletRequest request) throws WalletException {

        try {

            Wallet wallet = repository.findById(request.walletId())
                    .orElseThrow(() -> new WalletException(WalletErrorCodeEnum.NOT_FOUND_WALLET_BY_ID));

            updateWalletBalance(wallet, request);

            Wallet saved = repository.saveAndFlush(wallet);
            return mapper.mapToDto(saved);

        } catch (OptimisticLockingFailureException e) {
            throw new WalletException(WalletErrorCodeEnum.CONCURRENT_MODIFICATION);
        }

    }

    private void updateWalletBalance(Wallet wallet, WalletRequest request) throws WalletException {

        if (OperationType.WITHDRAW.equals(request.operationType())) {

            if (wallet.amount().compareTo(request.amount()) < 0) {
                throw new WalletException(WalletErrorCodeEnum.INSUFFICIENT_FUNDS);
            }

            wallet.amount(wallet.amount().subtract(request.amount()));

        } else {
            wallet.amount(wallet.amount().add(request.amount()));
        }

    }


    @Override
    public WalletDto getWalletById(@PathVariable UUID walletId) throws WalletException {

        Wallet wallet = repository.findById(walletId)
                .orElseThrow(() -> new WalletException(WalletErrorCodeEnum.NOT_FOUND_WALLET_BY_ID));

        return mapper.mapToDto(wallet);

    }

}

package com.cotel.catherine.wallet_service.controller;

import com.cotel.catherine.wallet_service.TestContainerInitialization;
import com.cotel.catherine.wallet_service.entity.Wallet;
import com.cotel.catherine.wallet_service.repository.WalletRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WalletControllerTest extends TestContainerInitialization {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    private UUID firstWalletId;

    @BeforeEach
    void setUp() {

        walletRepository.deleteAll();

        firstWalletId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        Wallet wallet1 = Wallet.builder()
                .walletId(firstWalletId)
                .amount(new BigDecimal("5000.00"))
                .build();
        walletRepository.save(wallet1);

    }

    @AfterEach
    void tearDown() {
        walletRepository.deleteAll();
    }

    @Test
    void updateWallet_whenValidDepositRequest_thenSuccess() throws Exception {

        String depositJson = String.format("""
            {
                "walletId": "%s",
                "operationType": "DEPOSIT",
                "amount": 2000
            }
            """, firstWalletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(firstWalletId.toString()))
                .andExpect(jsonPath("$.amount").value(7000.0)); // 5000 + 2000 = 7000

    }

    @Test
    void updateWallet_whenValidWithdrawRequest_thenSuccess() throws Exception {
        String withdrawJson = String.format("""
            {
                "walletId": "%s",
                "operationType": "WITHDRAW",
                "amount": 3000
            }
            """, firstWalletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(firstWalletId.toString()))
                .andExpect(jsonPath("$.amount").value(2000.0));
    }

    @Test
    void updateWallet_whenWithdrawAllBalance_thenSuccess() throws Exception {
        String withdrawJson = String.format("""
            {
                "walletId": "%s",
                "operationType": "WITHDRAW",
                "amount": 5000
            }
            """, firstWalletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(firstWalletId.toString()))
                .andExpect(jsonPath("$.amount").value(0.0));
    }

    @Test
    void updateWallet_whenMultipleOperations_thenSuccess() throws Exception {

        UUID multiOpWalletId = UUID.randomUUID();
        Wallet multiOpWallet = Wallet.builder()
                .walletId(multiOpWalletId)
                .amount(new BigDecimal("3000.00"))
                .build();
        walletRepository.save(multiOpWallet);

        String depositJson = String.format("""
                {
                    "walletId": "%s",
                    "operationType": "DEPOSIT",
                    "amount": 2000
                }
                """, multiOpWalletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(depositJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(5000.0));

        String withdrawJson = String.format("""
                {
                    "walletId": "%s",
                    "operationType": "WITHDRAW",
                    "amount": 1500
                }
                """, multiOpWalletId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(withdrawJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(3500.0));

    }

    @Test
    void updateWallet_whenInvalidUUIDFormat_thenBadRequest() throws Exception {

        String jsonWithInvalidUUID = """
            {
                "walletId": "uuid",
                "operationType": "DEPOSIT",
                "amount": 1000
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithInvalidUUID))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenInvalidOperationType_thenBadRequest() throws Exception {

        String jsonWithInvalidOperation = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "operationType": "operation",
                "amount": 1000
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithInvalidOperation))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenMissingRequiredField_thenBadRequest() throws Exception {

        String jsonMissingAmount = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "operationType": "DEPOSIT"
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMissingAmount))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenMissingWalletId_thenBadRequest() throws Exception {

        String jsonMissingWalletId = """
            {
                "operationType": "DEPOSIT",
                "amount": 1000
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMissingWalletId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenMissingOperationType_thenBadRequest() throws Exception {

        String jsonMissingOperationType = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "amount": 1000
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMissingOperationType))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenNonExistWallet_thenNotFound() throws Exception {

        UUID nonExistentId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        String validJson = String.format("""
            {
                "walletId": "%s",
                "operationType": "DEPOSIT",
                "amount": 1000
            }
            """, nonExistentId);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("02"))
                .andExpect(jsonPath("$.description").value("Такой кошелек не существует"));

    }

    @Test
    void updateWallet_whenEmptyBody_thenBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateWallet_whenNegativeAmount_thenBadRequest() throws Exception {

        String jsonWithNegativeAmount = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "operationType": "DEPOSIT",
                "amount": -1000
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithNegativeAmount))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void updateWallet_whenZeroAmount_thenBadRequest() throws Exception {

        String jsonWithZeroAmount = """
            {
                "walletId": "123e4567-e89b-12d3-a456-426614174000",
                "operationType": "DEPOSIT",
                "amount": 0
            }
            """;

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonWithZeroAmount))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("01"))
                .andExpect(jsonPath("$.description").value("Невалидный json"));

    }

    @Test
    void getWalletById_whenWalletExists_thenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{WALLET_UUID}", firstWalletId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(firstWalletId.toString()))
                .andExpect(jsonPath("$.amount").value(5000.0));
    }


    @Test
    void getWalletById_whenInvalidPathVariable_thenBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/uuid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("02"))
                .andExpect(jsonPath("$.description").value("Такой кошелек не существует"));
    }

    @Test
    void getWalletById_whenNonExistWallet_thenNotFound() throws Exception {

        UUID nonExistentId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        mockMvc.perform(get("/api/v1/wallets/{WALLET_UUID}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("02"))
                .andExpect(jsonPath("$.description").value("Такой кошелек не существует"));

    }


    @Test
    void getWalletById_whenInvalidPath_thenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/wallets"))
                .andExpect(status().is5xxServerError());
    }

}
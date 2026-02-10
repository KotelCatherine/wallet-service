package com.cotel.catherine.wallet_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallet")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class Wallet {

    @Id
    @Column(name = "wallet_id")
    private UUID walletId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Version
    @Column(name = "version")
    private Long version;

}

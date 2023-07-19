package com.silkpay.techtask.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;
}

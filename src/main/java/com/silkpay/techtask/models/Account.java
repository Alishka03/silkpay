package com.silkpay.techtask.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ACCOUNT")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User user;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL)
    private Set<Transaction> receivedTransactions;
}

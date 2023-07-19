package com.silkpay.techtask.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
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
    @JsonIgnore
    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL)
    private Set<Transaction> sentTransactions = new HashSet<>();
    @JsonIgnore
    @OneToMany(mappedBy = "receiverAccount", cascade = CascadeType.ALL)
    private Set<Transaction> receivedTransactions = new HashSet<>();
}

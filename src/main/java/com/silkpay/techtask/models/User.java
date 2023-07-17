package com.silkpay.techtask.models;



import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Account> accounts;
}

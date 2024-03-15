package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "account", schema = "demo", catalog = "demo")
public class AccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long accountPoolId;
    private String accountNumber;
    private Boolean bussy;
}

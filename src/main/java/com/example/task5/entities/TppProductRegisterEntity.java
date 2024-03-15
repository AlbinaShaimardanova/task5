package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "tpp_product_register", schema = "demo", catalog = "demo")
public class TppProductRegisterEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long productId;
    private String type;
    private Long account;
    private String currencyCode;
    private String state;
    private String accountNumber;
}

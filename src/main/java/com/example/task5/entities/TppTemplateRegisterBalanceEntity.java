package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "tpp_template_register_balance", schema = "demo", catalog = "demo")
public class TppTemplateRegisterBalanceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long registerId;
    private BigInteger amount;
    private String order;
    private Timestamp lastModifyDate;
}

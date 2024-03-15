package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "account_pool", schema = "demo", catalog = "demo")
public class AccountPoolEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String branchCode;
    private String currencyCode;
    private String mdmCode;
    private String priorityCode;
    private String registryTypeCode;

}

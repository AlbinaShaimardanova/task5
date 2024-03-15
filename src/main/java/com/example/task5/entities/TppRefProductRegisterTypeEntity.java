package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@Table(name = "tpp_ref_product_register_type", schema = "demo", catalog = "demo")
public class TppRefProductRegisterTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long internalId;
    private String value;
    private String registerTypeName;
    private String productClassCode;
    private Timestamp registerTypeStartDate;
    private Timestamp registerTypeEndDate;
    private String accountType;

}

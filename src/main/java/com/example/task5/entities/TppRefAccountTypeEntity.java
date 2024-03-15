package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "tpp_ref_account_type", schema = "demo", catalog = "demo")
public class TppRefAccountTypeEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long internalId;
    private String value;
}

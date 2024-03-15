package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tpp_ref_product_class", schema = "demo", catalog = "demo")
public class TppRefProductClassEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long internalId;
    private String value;
    private String gbiCode;
    private String gbiName;
    private String productRowCode;
    private String productRowName;
    private String subclassCode;
    private String subclassName;
}

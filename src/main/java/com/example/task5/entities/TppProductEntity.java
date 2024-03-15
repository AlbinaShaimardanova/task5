package com.example.task5.entities;

import com.example.task5.enums.ProductType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "tpp_product", schema = "demo", catalog = "demo")
public class TppProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long productCodeId;
    private Long clientId;
    private ProductType type;
    private String number;
    private Long priority;
    private Date dateOfConclusion;
    private Date startDateTime;
    private Date endDateTime;
    private Long days;
    private Double penaltyRate;
    private Double nso;
    private Double thresholdAmount;
    private String requisiteType;
    private String interestRateType;
    private Double taxRate;
    private String reasoneClose;
    private String state;
}

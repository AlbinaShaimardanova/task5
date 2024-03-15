package com.example.task5.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
@Table(name = "agreement", schema = "demo", catalog = "demo")
public class AgreementEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long productId;
    private String generalAgreementId;
    private String supplementaryAgreementId;
    private String arrangementType;
    private Long shedulerJobId;
    private String number;
    private Date openingDate;
    private Date closingDate;
    private Date cancelDate;
    private Long validityDuration;
    private String cancellationReason;
    private String status;
    private Date interestCalculationDate;
    private Double interestRate;
    private Double coefficient;
    private String coefficientAction;
    private Double minimumInterestRate;
    private Double minimumInterestRateCoefficient;
    private String minimumInterestRateCoefficientAction;
    private Double maximalInterestRate;
    private Double maximalInterestRateCoefficient;
    private String maximalInterestRateCoefficientAction;
}

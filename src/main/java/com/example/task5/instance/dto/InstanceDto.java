package com.example.task5.instance.dto;

import com.example.task5.entities.AgreementEntity;
import com.example.task5.enums.ProductType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class InstanceDto {
    Long instanceId;
    ProductType productType;
    String productCode;
    String registerType;
    String mdmCode;
    String contractNumber;
    Date contractDate;
    Long priority;
    Double interestRatePenalty;
    Double minimalBalance;
    Double thresholdAmount;
    String accountingDetails;
    String rateType;
    Double taxPercentageRate;
    Double technicalOverdraftLimitAmount;
    Integer contractId;
    String branchCode;
    String isoCurrencyCode;
    String urgencyCode;
    Integer referenceCode;
    List<AddPropDto> additionalPropertiesVip;
    List<AgreementEntity> agreements;
}

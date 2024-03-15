package com.example.task5.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
    private Long instanceId;
    private String registryTypeCode;
    private String accountType;
    private String currencyCode;
    private String branchCode;
    private String priorityCode;
    private String mdmCode;
    private String clientCode;
    private String trainRegion;
    private String counter;
    private String salesCode;
}

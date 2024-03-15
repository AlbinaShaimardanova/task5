package com.example.task5.instance.controller;

import com.example.task5.entities.*;
import com.example.task5.enums.ProductRegisterState;
import com.example.task5.instance.dto.InstanceDto;
import com.example.task5.repo.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/corporate-settlement-instance")
public class InstanceRestController {
    AccountRepo accountRepo;
    AccountPoolRepo accountPoolRepo;
    AgreementRepo agreementRepo;
    TppProductRepo tppProductRepo;
    TppRefProductClassRepo tppRefProductClassRepo;
    TppProductRegisterRepo tppProductRegisterRepo;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody InstanceDto instanceDto) {
        if (instanceDto.getProductType() == null) return response("productType");
        if (instanceDto.getProductCode() == null) return response("productCode");
        if (instanceDto.getRegisterType() == null) return response("registerType");
        if (instanceDto.getMdmCode() == null) return response("mdmCode");
        if (instanceDto.getContractNumber() == null) return response("contractNumber");
        if (instanceDto.getContractDate() == null) return response("contractDate");
        if (instanceDto.getPriority() == null) return response("priority");
        if (instanceDto.getContractId() == null) return response("contractId");
        if (instanceDto.getBranchCode() == null) return response("BranchCode");
        if (instanceDto.getIsoCurrencyCode() == null) return response("IsoCurrencyCode");
        if (instanceDto.getUrgencyCode() == null) return response("urgencyCode");

        List<AgreementEntity> agreements = instanceDto.getAgreements();
        for (AgreementEntity agreement : agreements) {
            if (agreement.getNumber() == null) return response("Number");
            if (agreement.getOpeningDate() == null) return response("openingDate");
        }

        try {
            if (instanceDto.getInstanceId() == null) {
                TppProductEntity tppProduct = tppProductRepo.findByNumber(instanceDto.getContractNumber());
                if (tppProduct != null) {
                    String response = "Параметр ContractNumber № договора <" + tppProduct.getNumber() + "> уже существует.";
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                for(var agr : instanceDto.getAgreements()){
                    AgreementEntity agreement = agreementRepo.findAgreementByNumber(agr.getNumber());
                    if (agreement != null) {
                        String response = "Параметр № Дополнительного соглашения (сделки) Number <" + agr.getNumber() + "> уже существует";
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                }

                tppProduct = saveProduct(instanceDto);
                AccountEntity account = findAccount(instanceDto);
                TppProductRegisterEntity tppProductRegister = saveTppProductRegister(instanceDto, tppProduct, account);
                List<TppProductRegisterEntity> tppProductRegisters = new ArrayList<>();
                tppProductRegisters.add(tppProductRegister);

                return new ResponseEntity<>(buildOkResponse(instanceDto, tppProductRegisters), HttpStatus.OK);
            } else {
                Optional<TppProductEntity> tppProduct = tppProductRepo.findById(instanceDto.getInstanceId());
                if (tppProduct.isEmpty()){
                    String response = "Экземпляр продукта с параметром instanceId <"+
                            instanceDto.getInstanceId()+
                            "> не найден";
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }

                for(var agr : instanceDto.getAgreements()){
                    AgreementEntity agreement = agreementRepo.findAgreementByNumber(agr.getNumber());
                    if (agreement != null) {
                        String response = "Параметр № Дополнительного соглашения (сделки) Number <"+agr.getNumber()+"> уже существует";
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    } else {
                        agr.setProductId(tppProduct.get().getId());
                        agreementRepo.save(agr);
                    }
                }

                List<TppProductRegisterEntity> tppProductRegisters = tppProductRegisterRepo.findTppProductRegisterByProductId(tppProduct.get().getId());

                return new ResponseEntity<>(buildOkResponse(instanceDto, tppProductRegisters), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> response(String nameVal) {
        var response = "Обязательный параметр <" + nameVal + "> не заполнен.";
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private String buildOkResponse(InstanceDto instanceDto,
                                   List<TppProductRegisterEntity> tppProductRegisters){
        JsonArrayBuilder joTppProductRegisters = Json.createArrayBuilder();
        tppProductRegisters.forEach((tppProductRegister) -> joTppProductRegisters.add(tppProductRegister.getId()));

        JsonArrayBuilder joAgreements = Json.createArrayBuilder();
        for(var agreement : instanceDto.getAgreements()){
            if (agreement.getId() != null) {
                joAgreements.add(agreement.getId());
            }
        }

        return Json.createObjectBuilder()
                .add("data", Json.createObjectBuilder()
                        .add("instanceId", String.valueOf(instanceDto.getInstanceId()))
                        .add("registerId", joTppProductRegisters)
                        .add("supplementaryAgreementId", joAgreements))
                .build()
                .toString();
    }

    private TppProductEntity saveProduct(InstanceDto instanceDto){
        TppProductEntity tppProduct = new TppProductEntity();
        tppProduct.setProductCodeId(tppRefProductClassRepo.findByValue(instanceDto.getProductCode()).getInternalId());
        tppProduct.setType(instanceDto.getProductType());
        tppProduct.setNumber(instanceDto.getContractNumber());
        tppProduct.setPriority(instanceDto.getPriority());
        tppProduct.setDateOfConclusion(instanceDto.getContractDate());
        tppProduct.setStartDateTime(instanceDto.getContractDate());
        tppProduct.setThresholdAmount(instanceDto.getThresholdAmount());
        tppProduct.setTaxRate(instanceDto.getTaxPercentageRate());
        tppProductRepo.save(tppProduct);

        return tppProduct;
    }

    private AccountEntity findAccount(InstanceDto instanceDto){
        AccountPoolEntity accountPool = accountPoolRepo.findAccountPoolByAllReq(
                instanceDto.getBranchCode(),
                instanceDto.getIsoCurrencyCode(),
                instanceDto.getMdmCode(),
                instanceDto.getUrgencyCode(),
                instanceDto.getRegisterType());

        return accountRepo.findFirstByAccountPoolIdOrderById(accountPool.getId());
    }

    private TppProductRegisterEntity saveTppProductRegister(InstanceDto instanceDto, TppProductEntity tppProduct, AccountEntity account){
        TppProductRegisterEntity tppProductRegister = new TppProductRegisterEntity();
        tppProductRegister.setProductId(tppProduct.getId());
        tppProductRegister.setType(instanceDto.getRegisterType());
        tppProductRegister.setAccount(account.getId());
        tppProductRegister.setCurrencyCode(instanceDto.getIsoCurrencyCode());
        tppProductRegister.setAccountNumber(account.getAccountNumber());
        tppProductRegister.setState(String.valueOf(ProductRegisterState.OPEN));

        tppProductRegisterRepo.save(tppProductRegister);

        return tppProductRegister;
    }
}

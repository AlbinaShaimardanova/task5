package com.example.task5.account.controller;

import com.example.task5.account.dto.AccountDto;
import com.example.task5.entities.AccountEntity;
import com.example.task5.entities.AccountPoolEntity;
import com.example.task5.entities.TppProductRegisterEntity;
import com.example.task5.entities.TppRefProductRegisterTypeEntity;
import com.example.task5.enums.ProductRegisterState;
import com.example.task5.repo.AccountPoolRepo;
import com.example.task5.repo.AccountRepo;
import com.example.task5.repo.TppProductRegisterRepo;
import com.example.task5.repo.TppRefProductRegisterTypeRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.json.Json;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/corporate-settlement-account")
public class AccountRestController {

    TppProductRegisterRepo tppProductRegisterRepo;
    AccountRepo accountRepo;
    AccountPoolRepo accountPoolRepo;
    TppRefProductRegisterTypeRepo tppRefProductRegisterTypeRepo;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody AccountDto accountDTO) {
        log.info("Получен запрос POST account");

        if (accountDTO.getInstanceId() == null) {
            var response = "Параметр <instanceId> не заполнен.";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        TppProductRegisterEntity tppProductRegister = tppProductRegisterRepo.findByProductIdAndType(accountDTO.getInstanceId(), accountDTO.getRegistryTypeCode());
        if (tppProductRegister != null) {
            var response = "Параметр registryTypeCode тип регистра <" +
                    tppProductRegister.getType() +
                    "> уже существует для ЭП с ИД  <" +
                    tppProductRegister.getProductId() +
                    ">.";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        TppRefProductRegisterTypeEntity tppRefProductRegisterType = tppRefProductRegisterTypeRepo.findByValue(accountDTO.getRegistryTypeCode());
        if (tppRefProductRegisterType == null) {
            String response = "Код Продукта <" +
                    accountDTO.getRegistryTypeCode() +
                    "> не найдено в Каталоге продуктов <" +
                    "springtask_tst_schema.tpp_ref_product_register_type" +
                    "> для данного типа Регистра";
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            AccountEntity account = findAccount(accountDTO);
            saveTppProductRegister(accountDTO, account);

            return new ResponseEntity<>(buildOkResponse(account), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private String buildOkResponse(AccountEntity account) {
        return Json.createObjectBuilder()
                .add("data", Json.createObjectBuilder()
                        .add("accountId", account.getId()))
                .build()
                .toString();
    }

    private AccountEntity findAccount(AccountDto accountDTO) {
        AccountPoolEntity accountPool = accountPoolRepo.findAccountPoolByAllReq(
                accountDTO.getBranchCode(),
                accountDTO.getCurrencyCode(),
                accountDTO.getMdmCode(),
                accountDTO.getPriorityCode(),
                accountDTO.getRegistryTypeCode());

        return accountRepo.findFirstByAccountPoolIdOrderById(accountPool.getId());
    }

    private void saveTppProductRegister(AccountDto accountDTO, AccountEntity account) {
        TppProductRegisterEntity tppProductRegister = new TppProductRegisterEntity();
        tppProductRegister.setProductId(accountDTO.getInstanceId());
        tppProductRegister.setType(accountDTO.getRegistryTypeCode());
        tppProductRegister.setAccount(account.getId());
        tppProductRegister.setCurrencyCode(accountDTO.getCurrencyCode());
        tppProductRegister.setAccountNumber(account.getAccountNumber());
        tppProductRegister.setState(String.valueOf(ProductRegisterState.OPEN));

        tppProductRegisterRepo.save(tppProductRegister);
    }

}

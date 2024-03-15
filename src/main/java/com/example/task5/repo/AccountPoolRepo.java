package com.example.task5.repo;

import com.example.task5.entities.AccountPoolEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AccountPoolRepo extends CrudRepository<AccountPoolEntity, Long> {
    @Query(value = "SELECT ap " +
            "FROM AccountPoolEntity ap " +
            "WHERE ap.branchCode = :branchCode" +
            " and ap.currencyCode = :currencyCode" +
            " and ap.mdmCode = :mdmCode" +
            " and ap.priorityCode = :priorityCode" +
            " and ap.registryTypeCode = :registryTypeCode")
    AccountPoolEntity findAccountPoolByAllReq(String branchCode, String currencyCode, String mdmCode, String priorityCode, String registryTypeCode);
}

package com.example.task5.repo;

import com.example.task5.entities.AgreementEntity;
import org.springframework.data.repository.CrudRepository;

public interface AgreementRepo extends CrudRepository<AgreementEntity, Long> {
    AgreementEntity findAgreementByNumber(String number);
}

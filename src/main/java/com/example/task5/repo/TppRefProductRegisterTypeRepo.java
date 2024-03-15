package com.example.task5.repo;

import com.example.task5.entities.TppRefProductRegisterTypeEntity;
import org.springframework.data.repository.CrudRepository;

public interface TppRefProductRegisterTypeRepo extends CrudRepository<TppRefProductRegisterTypeEntity, Long> {
    TppRefProductRegisterTypeEntity findByValue(String value);
}

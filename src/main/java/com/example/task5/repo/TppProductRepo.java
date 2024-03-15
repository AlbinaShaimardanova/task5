package com.example.task5.repo;

import com.example.task5.entities.TppProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface TppProductRepo extends CrudRepository<TppProductEntity, Long> {
    TppProductEntity findByNumber(String number);
}

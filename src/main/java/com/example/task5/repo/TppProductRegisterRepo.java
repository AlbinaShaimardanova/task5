package com.example.task5.repo;

import com.example.task5.entities.TppProductRegisterEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TppProductRegisterRepo extends CrudRepository<TppProductRegisterEntity, Long> {
    TppProductRegisterEntity findByProductIdAndType(long productId, String type);
    List<TppProductRegisterEntity> findTppProductRegisterByProductId(long productId);
}

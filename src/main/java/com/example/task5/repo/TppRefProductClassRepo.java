package com.example.task5.repo;

import com.example.task5.entities.TppRefProductClassEntity;
import org.springframework.data.repository.CrudRepository;

public interface TppRefProductClassRepo extends CrudRepository<TppRefProductClassEntity, Long> {
    TppRefProductClassEntity findByValue(String value);
}

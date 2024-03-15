package com.example.task5.repo;

import com.example.task5.entities.AccountEntity;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepo extends CrudRepository<AccountEntity, Long> {
    AccountEntity findFirstByAccountPoolIdOrderById(long accountPoolId);
}

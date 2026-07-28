package com.vbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbank.model.Transaction;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, String>{
   List<Transaction> findByFromAccountIdOrToAccountId(String fromAccountId, String toAccountId);


}

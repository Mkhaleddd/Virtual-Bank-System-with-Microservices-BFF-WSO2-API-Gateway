package com.vbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String>{

}

package com.vbank.controller;

import com.vbank.dto.TransferExecutionRequest;
import com.vbank.dto.TransferInitiationRequest;
import com.vbank.model.Transaction;
import com.vbank.service.TransactionService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getTransactions();
    }
        
    // POST /transactions/transfer/initiation: Initiates a fund transfer between two accounts
    @PostMapping("transactions/transfer/initiation")
    public ResponseEntity<Map<String, Object>> initiateTransfer(@Valid @RequestBody TransferInitiationRequest req) {
        Map<String, Object> response = transactionService.initiateTransfer(req);
        return ResponseEntity.ok(response);
    }

    // POST /transactions/transfer/execution: Execute fund transfer between two accounts
    @PostMapping("transactions/transfer/execution")
    public ResponseEntity<Map<String, Object>> executeTransfer(@Valid @RequestBody TransferExecutionRequest req) {
        Map<String, Object> response = transactionService.executeTransfer(req);
        return ResponseEntity.ok(response);
    }

    // GET /accounts/{accountId}/transactions: Retrieves the transaction history for a specific account
    @GetMapping("accounts/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId(@PathVariable("accountId") String accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}
package com.vbank.service;

import com.vbank.dto.TransferExecutionRequest;
import com.vbank.dto.TransferInitiationRequest;
import com.vbank.annotation.LoggableEvent;
import com.vbank.model.Transaction;
import com.vbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    @LoggableEvent(eventType = "TRANSFER_INITIATED")
    public Map<String, Object> initiateTransfer(TransferInitiationRequest req) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(req.getFromAccountId());
        transaction.setToAccountId(req.getToAccountId());
        transaction.setAmount(req.getAmount());
        transaction.setDescription(req.getDescription());
        transaction.setTimestamp(Instant.now());
        transaction.setDeliveryStatus("INITIATED");

        Transaction savedTransaction = transactionRepository.save(transaction);

        return Map.of(
            "transactionId", savedTransaction.getTransactionId(),
            "status", savedTransaction.getDeliveryStatus(),
            "timestamp", savedTransaction.getTimestamp().toString()
        );
    }

    @Transactional
    @LoggableEvent(eventType = "TRANSFER_EXECUTE")
    public Map<String, Object> executeTransfer(TransferExecutionRequest req) {
        if (req == null || req.getTransactionId() == null) {
            throw new IllegalArgumentException("Transaction execution request or ID cannot be null");
        }

        Transaction transaction = transactionRepository.findById(req.getTransactionId()).orElse(null);
        if (transaction == null || transaction.getAmount() == null) {
            throw new RuntimeException("Transaction not found with ID: " + req.getTransactionId());
        }

        // TODO: Add call to account-service to process balance deduction/addition here
        transaction.setDeliveryStatus("SUCCESS");
        Transaction savedTransaction = transactionRepository.save(transaction);

        return Map.of(
            "transactionId", savedTransaction.getTransactionId(),
            "status", savedTransaction.getDeliveryStatus(),
            "timestamp", savedTransaction.getTimestamp().toString()
        );
    }

    public List<Transaction> getTransactionsByAccountId(String id) {
        List<Transaction> transactions = transactionRepository.findByFromAccountIdOrToAccountId(id, id);
        if (transactions.isEmpty()) {
            throw new RuntimeException("No transactions found for account ID " + id);
        }
        return transactions;
    }
}
package com.vbank.service;
import com.vbank.model.Transaction;
import com.vbank.dto.TransferExecutionRequest;
import com.vbank.dto.TransferInitiationRequest;
import com.vbank.repository.TransactionRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
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

    public Map<String,Object> initiateTransfer(TransferInitiationRequest req){
        Transaction transaction = new Transaction();
        transaction.setFromAccountId(req.getFromAccountId());
        transaction.setToAccountId(req.getToAccountId());
        transaction.setAmount(req.getAmount());
        transaction.setDescription(req.getDescription());
        transaction.setTimestamp(Instant.now());
        transaction.setDeliveryStatus("Initiated");
        Transaction savedTransaction = transactionRepository.save(transaction);
        return Map.of(
            "transactionId", savedTransaction.getTransactionId(),
            "status", savedTransaction.getDeliveryStatus(),
            "timestamp", savedTransaction.getTimestamp().toString()
        );
    }


    public Map<String,Object> executeTransfer (TransferExecutionRequest req){
        Transaction transaction = transactionRepository.findById(req.getTransactionId()).orElse(null);
        if (transaction == null || transaction.getAmount() == null) return null;
        // add account service here
        try {
            transaction.setDeliveryStatus("Success");

        } catch (Exception e) {
           transaction.setDeliveryStatus("Failed");
        }

        Transaction savedTransaction=transactionRepository.save(transaction);
        return Map.of(
            "transactionId", savedTransaction.getTransactionId(),
            "status", savedTransaction.getDeliveryStatus(),
            "timestamp", savedTransaction.getTimestamp().toString()
        );
    }

    public List<Transaction> getTransactionsByAccountId(String id){
        List<Transaction> transactions = transactionRepository.findByFromAccountIdOrToAccountId(id, id);
        if (transactions.isEmpty()) {
            throw new RuntimeException("No transactions found for account ID " + id);
        }
        return transactions;
    }
}
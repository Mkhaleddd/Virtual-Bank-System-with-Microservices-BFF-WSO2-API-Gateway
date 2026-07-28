package com.vbank.service;

import com.vbank.dto.TransferExecutionRequest;
import com.vbank.dto.TransferInitiationRequest;
import com.vbank.annotation.LoggableEvent;
import com.vbank.model.Transaction;
import com.vbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;

    private final String ACCOUNT_SERVICE_URL = "http://localhost:8082/accounts/transfer";

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
                "timestamp", savedTransaction.getTimestamp().toString());
    }

    @Transactional
    @LoggableEvent(eventType = "TRANSFER_EXECUTE")
    public Map<String, Object> executeTransfer(TransferExecutionRequest req) {
        if (req == null || req.getTransactionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction ID cannot be null");
        }

        Transaction transaction = transactionRepository.findById(req.getTransactionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid 'from' or 'to' account ID."));

        if (!"INITIATED".equals(transaction.getDeliveryStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction is already processed or invalid.");
        }
       
        try {
         
            Map<String, Object> balanceRequest = Map.of(
                    "fromAccountId", transaction.getFromAccountId(),
                    "toAccountId", transaction.getToAccountId(),
                    "amount", transaction.getAmount());

            restTemplate.put(ACCOUNT_SERVICE_URL, balanceRequest);

        } catch (HttpClientErrorException ex) {
            transaction.setDeliveryStatus("FAILED");
            transactionRepository.save(transaction);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getResponseBodyAsString());
        } catch (Exception ex) {
            transaction.setDeliveryStatus("FAILED");
            transactionRepository.save(transaction);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to communicate with Account Service.");
        }
        transaction.setDeliveryStatus("SUCCESS");
        Transaction savedTransaction = transactionRepository.save(transaction);

        return Map.of(
                "transactionId", savedTransaction.getTransactionId(),
                "status", savedTransaction.getDeliveryStatus(),
                "timestamp", savedTransaction.getTimestamp().toString());
    }

    public List<Transaction> getTransactionsByAccountId(String id) {
        List<Transaction> transactions = transactionRepository.findByFromAccountIdOrToAccountId(id, id);
        if (transactions.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transactions found for account ID " + id + ".");
        }
        return transactions;
    }
}
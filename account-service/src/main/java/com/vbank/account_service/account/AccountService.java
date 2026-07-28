package com.vbank.account_service.account;

import com.vbank.account_service.account.dto.*;
import com.vbank.account_service.account.exception.InsufficientFundsException;
import com.vbank.account_service.account.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    private static final String ACCOUNT_URL = "http://localhost:8081/users/";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public AccountService(AccountRepository accountRepository, RestTemplate restTemplate) {
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public UpdateBalanceResponse updateBalance(UpdateBalanceRequest updateBalanceRequest) {
        if (updateBalanceRequest.getFromAccountId().equals(updateBalanceRequest.getToAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        Account fromAccount = accountRepository.findById(updateBalanceRequest.getFromAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found: " + updateBalanceRequest.getFromAccountId()));
        Account toAccount = accountRepository.findById(updateBalanceRequest.getToAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found: " + updateBalanceRequest.getToAccountId()));

        if (fromAccount.getBalance().compareTo(updateBalanceRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account " + fromAccount.getId());
        }
        accountRepository.addToBalance(updateBalanceRequest.getAmount(), updateBalanceRequest.getToAccountId());
        accountRepository.subtractFromBalance(updateBalanceRequest.getAmount(), updateBalanceRequest.getFromAccountId());

        fromAccount.setStatus(AccountStatus.ACTIVE);
        toAccount.setStatus(AccountStatus.ACTIVE);

        return new UpdateBalanceResponse(
                "Account updated successfully."
        );
    }

    public BankAccountResponse getBankAccount(UUID accountId) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("This account does not exist"));

        return new BankAccountResponse(
                account.getId(),
                account.getNumber(),
                account.getBalance(),
                account.getType(),
                account.getStatus()
        );
    }

    public List<BankAccountResponse> getBankAccountsByUser(UUID userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);

        if (accounts.isEmpty())
            throw new NotFoundException("No Accounts found for User ID: " + userId);
        List<BankAccountResponse> responses = new ArrayList<>();
        for (Account account : accounts) {
            responses.add(new BankAccountResponse(
                    account.getId(),
                    account.getNumber(),
                    account.getBalance(),
                    account.getType(),
                    account.getStatus()
            ));
        }

        return responses;
    }

    public CreateResponse createAccount(CreateRequest createRequest) {
        Account account = new Account();

        if (Boolean.TRUE.equals(restTemplate.getForObject(ACCOUNT_URL + createRequest.getUserId() + "/exists", boolean.class))) {
            account.setUserId(createRequest.getUserId());
            account.setStatus(AccountStatus.ACTIVE);
            account.setBalance(createRequest.getInitialAmount());
            account.setType(createRequest.getAccountType());
            account.setNumber(generateUniqueAccountNumber());
            accountRepository.save(account);
        } else throw new NotFoundException("Account not found: " + createRequest.getUserId());
        return new CreateResponse(
                account.getId(),
                account.getNumber(),
                "Account created successfully."
        );
    }

    private String generateAccountNumber() {
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(RANDOM.nextInt(9) + 1);

        for (int i = 1; i < 10; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }

        return accountNumber.toString();
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;

        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByNumber(accountNumber));

        return accountNumber;
    }

}

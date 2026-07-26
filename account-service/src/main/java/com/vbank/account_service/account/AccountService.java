package com.vbank.account_service.account;

import com.vbank.account_service.account.dto.BankAccountResponse;
import com.vbank.account_service.account.dto.UpdateBalanceRequest;
import com.vbank.account_service.account.dto.UpdateBalanceResponse;
import com.vbank.account_service.account.exception.InsufficientFundsException;
import com.vbank.account_service.account.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public UpdateBalanceResponse updateBalance(UpdateBalanceRequest updateBalanceRequest) {

        Account fromAccount = accountRepository.findById(updateBalanceRequest.getFromAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found: " + updateBalanceRequest.getFromAccountId()));
        Account toAccount = accountRepository.findById(updateBalanceRequest.getToAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found: " + updateBalanceRequest.getToAccountId()));

        if (fromAccount.getBalance().compareTo(updateBalanceRequest.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds in account " + fromAccount.getId());
        }
        accountRepository.addToBalance(updateBalanceRequest.getAmount(), updateBalanceRequest.getToAccountId());
        accountRepository.subtractFromBalance(updateBalanceRequest.getAmount(), updateBalanceRequest.getFromAccountId());

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

//    public CreateResponse createAccount(CreateRequest createRequest) {
//        if(accountRepository.existsBy)
//    }
}

package com.vbank.account_service.account;

import com.vbank.account_service.account.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/accounts/transfer")
    public UpdateBalanceResponse updateBalance(@Valid @RequestBody UpdateBalanceRequest updateBalanceRequest) {
        return accountService.updateBalance(updateBalanceRequest);
    }

    @PostMapping("/accounts")
    public CreateResponse createAccount(@Valid @RequestBody CreateRequest createRequest) {
        return accountService.createAccount(createRequest);
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountResponse getBankAccount(@PathVariable("accountId") UUID accountId) {
        return accountService.getBankAccount(accountId);
    }

    @GetMapping("users/{userId}/accounts")
    public List<BankAccountResponse> getBankAccountsByUser(@PathVariable("userId") UUID userId) {
        return accountService.getBankAccountsByUser(userId);
    }
}

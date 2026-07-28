package com.vbank.account_service.account;

import com.vbank.account_service.account.dto.BankAccountResponse;
import com.vbank.account_service.account.dto.UpdateBalanceRequest;
import com.vbank.account_service.account.dto.UpdateBalanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/transfer")
    public UpdateBalanceResponse updateBalance(@RequestBody UpdateBalanceRequest updateBalanceRequest) {
        return accountService.updateBalance(updateBalanceRequest);
    }

    // @PostMapping()
    // public CreateResponse createAccount(@RequestBody CreateRequest createRequest)
    // {
    // return accountService.createAccount(createRequest);
    // }

    @GetMapping("/{accountId}")
    public BankAccountResponse getBankAccount(@PathVariable("accountId") UUID accountId) {
        return accountService.getBankAccount(accountId);
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<BankAccountResponse>> getAccountsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }
}

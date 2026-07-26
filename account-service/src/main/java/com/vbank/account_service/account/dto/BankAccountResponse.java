package com.vbank.account_service.account.dto;

import com.vbank.account_service.account.AccountStatus;
import com.vbank.account_service.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BankAccountResponse {
    private UUID accountId;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;

}

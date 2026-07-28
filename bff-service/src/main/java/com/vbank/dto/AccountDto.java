package com.vbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import com.vbank.model.AccountType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String accountId;
    private long accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private List<TransactionDto> transactions;
}



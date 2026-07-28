package com.vbank.account_service.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequest {
    @NotNull(message = "from account is required")
    private UUID fromAccountId;

    @NotNull(message = "to account is required")
    private UUID toAccountId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount has to be greater than zero")
    private BigDecimal amount;
}

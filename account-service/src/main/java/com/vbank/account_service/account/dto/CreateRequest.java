package com.vbank.account_service.account.dto;

import com.vbank.account_service.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequest {
    @NotBlank(message = "userId is required")
    private UUID userId;

    @NotBlank(message = "account type is required")
    private AccountType accountType;

    @NotBlank(message = "initial amount is required")
    @PositiveOrZero(message = "Invalid Initial Amount")
    private BigDecimal initialAmount;
}

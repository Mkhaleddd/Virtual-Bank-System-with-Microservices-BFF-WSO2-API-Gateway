package com.vbank.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransferExecutionRequest {
    @NotNull(message = "ID is required")
    @NotBlank(message = "Transaction ID is required")
    private String transactionId;
}
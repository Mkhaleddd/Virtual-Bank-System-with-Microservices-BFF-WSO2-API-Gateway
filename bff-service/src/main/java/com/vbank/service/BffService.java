package com.vbank.service;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.vbank.dto.UserDto;
import com.vbank.dto.AccountDto;
import com.vbank.dto.DashboardDto;
import com.vbank.dto.TransactionDto;

@Service
@RequiredArgsConstructor
public class BffService {
    private final WebClient webClient;
    private final String USER_SERVICE_URL = "http://localhost:8081";
    private final String ACCOUNT_SERVICE_URL = "http://localhost:8082";
    private final String TRANSACTION_SERVICE_URL = "http://localhost:8084";

    public DashboardDto getDashboardData(UUID userId) {

        UserDto userInfoResponse = webClient.get().uri(USER_SERVICE_URL + "/users/" + userId).retrieve()
                .bodyToMono(UserDto.class).block();
        AccountDto[] accountsResponse = webClient.get().uri(ACCOUNT_SERVICE_URL + "/accounts/users/" + userId)
                .retrieve().bodyToMono(AccountDto[].class).block();
        if (accountsResponse != null) {
            for (AccountDto account : accountsResponse) {
                TransactionDto[] transactionsResponse = webClient.get()
                        .uri(TRANSACTION_SERVICE_URL + "/accounts/" + account.getAccountId() + "/transactions")
                        .retrieve()
                        .bodyToMono(TransactionDto[].class)
                        .block();

                if (transactionsResponse != null) {
                    account.setTransactions(Arrays.asList(transactionsResponse));
                }
            }
        }
        return new DashboardDto(
                userInfoResponse.getUserId(),
                userInfoResponse.getUsername(),
                userInfoResponse.getEmail(),
                userInfoResponse.getFirstName(),
                userInfoResponse.getLastName(),
                accountsResponse != null ? Arrays.asList(accountsResponse) : null);
    }
}
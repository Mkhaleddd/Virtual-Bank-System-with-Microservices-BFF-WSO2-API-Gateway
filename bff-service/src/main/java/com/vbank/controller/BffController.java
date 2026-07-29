package com.vbank.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vbank.dto.DashboardDto;
import com.vbank.service.BffService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bff/dashboard")
@RequiredArgsConstructor
public class BffController {
    private final BffService bffService;
@GetMapping("/{userId}")
    public ResponseEntity<DashboardDto> getUserDashboard(@PathVariable UUID userId) {
        return ResponseEntity.ok(bffService.getDashboardData(userId));

    }

}

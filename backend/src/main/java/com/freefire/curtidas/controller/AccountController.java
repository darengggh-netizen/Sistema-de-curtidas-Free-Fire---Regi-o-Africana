package com.freefire.curtidas.controller;

import com.freefire.curtidas.dto.AccountInfoResponse;
import com.freefire.curtidas.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
@Tag(name = "Conta", description = "Endpoints para gerenciar contas Free Fire")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/add")
    @Operation(summary = "Adicionar conta Free Fire")
    public ResponseEntity<AccountInfoResponse> addAccount(
            @RequestParam String ffAccountId,
            @RequestParam String ffUsername,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        AccountInfoResponse response = accountService.addAccount(userId, ffAccountId, ffUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Obter informações da conta")
    public ResponseEntity<AccountInfoResponse> getAccountInfo(@PathVariable UUID accountId) {
        AccountInfoResponse response = accountService.getAccountInfo(accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-accounts")
    @Operation(summary = "Obter minhas contas Free Fire")
    public ResponseEntity<List<AccountInfoResponse>> getMyAccounts(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<AccountInfoResponse> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/info/{ffAccountId}")
    @Operation(summary = "Obter data de criação da conta")
    public ResponseEntity<LocalDateTime> getAccountCreationDate(@PathVariable String ffAccountId) {
        LocalDateTime createdDate = accountService.getAccountCreatedDate(ffAccountId);
        return ResponseEntity.ok(createdDate);
    }
}

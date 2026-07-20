package com.freefire.curtidas.controller;

import com.freefire.curtidas.service.BooyahPassService;
import com.freefire.curtidas.entity.BooyahPass;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/booyah")
@RequiredArgsConstructor
@Tag(name = "Booyah Pass", description = "Endpoints para gerenciar Booyah Pass")
@SecurityRequirement(name = "Bearer Authentication")
public class BooyahPassController {

    private final BooyahPassService booyahPassService;

    @PostMapping("/enviar")
    @Operation(summary = "Enviar Booyah Pass")
    public ResponseEntity<BooyahPass> sendBooyahPass(
            @RequestParam UUID accountId,
            @RequestParam String receiverFfAccountId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        BooyahPass booyahPass = booyahPassService.sendBooyahPass(userId, accountId, receiverFfAccountId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(booyahPass);
    }
}

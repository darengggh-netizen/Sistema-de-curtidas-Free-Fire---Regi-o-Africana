package com.freefire.curtidas.controller;

import com.freefire.curtidas.dto.LikeRequest;
import com.freefire.curtidas.dto.LikeResponse;
import com.freefire.curtidas.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/likes")
@RequiredArgsConstructor
@Tag(name = "Likes", description = "Endpoints para gerenciar likes")
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/send")
    @Operation(summary = "Enviar likes para uma conta")
    public ResponseEntity<LikeResponse> sendLike(
            @RequestParam UUID accountId,
            @Valid @RequestBody LikeRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        LikeResponse response = likeService.sendLike(userId, accountId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-likes")
    @Operation(summary = "Obter meus likes enviados")
    public ResponseEntity<List<LikeResponse>> getMyLikes(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<LikeResponse> likes = likeService.getUserLikes(userId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/received/{ffAccountId}")
    @Operation(summary = "Obter likes recebidos por uma conta")
    public ResponseEntity<List<LikeResponse>> getReceivedLikes(@PathVariable String ffAccountId) {
        List<LikeResponse> likes = likeService.getReceivedLikes(ffAccountId);
        return ResponseEntity.ok(likes);
    }
}

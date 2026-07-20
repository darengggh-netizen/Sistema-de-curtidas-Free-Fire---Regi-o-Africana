package com.freefire.curtidas.service;

import com.freefire.curtidas.dto.LikeRequest;
import com.freefire.curtidas.dto.LikeResponse;
import com.freefire.curtidas.entity.*;
import com.freefire.curtidas.repository.FreeFireAccountRepository;
import com.freefire.curtidas.repository.LikeRepository;
import com.freefire.curtidas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final FreeFireAccountRepository accountRepository;
    private final UserRepository userRepository;
    private final RateLimitService rateLimitService;

    public LikeResponse sendLike(UUID userId, UUID accountId, LikeRequest request) {
        // Validar rate limit
        if (!rateLimitService.canSendLike(userId, request.getPlan())) {
            throw new IllegalArgumentException("Limite diário de likes atingido para seu plano");
        }

        FreeFireAccount senderAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        if (!senderAccount.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Acesso negado");
        }

        Like like = Like.builder()
                .senderAccount(senderAccount)
                .receiverFfAccountId(request.getReceiverFfAccountId())
                .quantity(request.getQuantity())
                .plan(request.getPlan())
                .status(TransactionStatus.PENDING)
                .build();

        Like savedLike = likeRepository.save(like);

        // Simular envio (em produção, conectar com API real do Free Fire)
        simulateLikeSend(savedLike);

        return mapToLikeResponse(savedLike);
    }

    public List<LikeResponse> getUserLikes(UUID userId) {
        List<FreeFireAccount> userAccounts = accountRepository.findByUserId(userId);
        return userAccounts.stream()
                .flatMap(account -> likeRepository.findBySenderAccountId(account.getId()).stream())
                .map(this::mapToLikeResponse)
                .collect(Collectors.toList());
    }

    public List<LikeResponse> getReceivedLikes(String receiverFfAccountId) {
        return likeRepository.findByReceiverFfAccountId(receiverFfAccountId)
                .stream()
                .map(this::mapToLikeResponse)
                .collect(Collectors.toList());
    }

    private void simulateLikeSend(Like like) {
        // Simular envio bem-sucedido
        like.setStatus(TransactionStatus.SUCCESS);
        like.setResponseMessage("Like enviado com sucesso!");
        likeRepository.save(like);
    }

    private LikeResponse mapToLikeResponse(Like like) {
        return LikeResponse.builder()
                .id(like.getId())
                .senderUsername(like.getSenderAccount().getFfUsername())
                .receiverFfAccountId(like.getReceiverFfAccountId())
                .quantity(like.getQuantity())
                .plan(like.getPlan().toString())
                .status(like.getStatus())
                .responseMessage(like.getResponseMessage())
                .createdAt(like.getCreatedAt())
                .updatedAt(like.getUpdatedAt())
                .build();
    }
}

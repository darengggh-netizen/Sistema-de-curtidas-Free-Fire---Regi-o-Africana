package com.freefire.curtidas.service;

import com.freefire.curtidas.entity.PlanType;
import com.freefire.curtidas.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final LikeRepository likeRepository;

    private static final int BASICO_LIMIT = 100;
    private static final int PREMIUM_LIMIT = 500;
    private static final int ELITE_LIMIT = -1; // Ilimitado

    public boolean canSendLike(UUID userId, PlanType plan) {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        
        int dailyLimit = getDailyLimit(plan);
        if (dailyLimit == -1) return true; // ELITE

        // Contar likes enviados hoje
        long likesToday = likeRepository.findAll().stream()
                .filter(like -> like.getSenderAccount().getUser().getId().equals(userId))
                .filter(like -> like.getCreatedAt().isAfter(today))
                .count();

        return likesToday < dailyLimit;
    }

    private int getDailyLimit(PlanType plan) {
        return switch (plan) {
            case BASICO -> BASICO_LIMIT;
            case PREMIUM -> PREMIUM_LIMIT;
            case ELITE -> ELITE_LIMIT;
        };
    }
}

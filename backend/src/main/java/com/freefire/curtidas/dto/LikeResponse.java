package com.freefire.curtidas.dto;

import com.freefire.curtidas.entity.TransactionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeResponse {

    private UUID id;
    private String senderUsername;
    private String receiverFfAccountId;
    private Integer quantity;
    private String plan;
    private TransactionStatus status;
    private String responseMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

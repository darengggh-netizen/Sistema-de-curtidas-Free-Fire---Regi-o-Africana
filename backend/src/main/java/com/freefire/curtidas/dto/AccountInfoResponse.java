package com.freefire.curtidas.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfoResponse {

    private UUID id;
    private String ffAccountId;
    private String ffUsername;
    private Integer level;
    private Integer experience;
    private LocalDateTime accountCreatedDate;
    private LocalDateTime lastSynced;
}

package com.freefire.curtidas.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildInfoResponse {

    private UUID id;
    private String guildId;
    private String guildName;
    private Integer level;
    private Integer totalMembers;
    private String leaderFfId;
    private String country;
    private Integer experiencePoints;
    private Integer totalLikesReceived;
    private String description;
    private LocalDateTime createdAt;
}

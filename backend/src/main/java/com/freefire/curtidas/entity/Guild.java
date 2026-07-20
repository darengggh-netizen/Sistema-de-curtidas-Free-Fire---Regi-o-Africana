package com.freefire.curtidas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "guilds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Guild {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String guildId;

    @Column(nullable = false)
    private String guildName;

    @Column(columnDefinition = "INT DEFAULT 1")
    private Integer level;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalMembers;

    private String leaderFfId;

    @Enumerated(EnumType.STRING)
    private CountryCode country;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer experiencePoints;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer totalLikesReceived;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

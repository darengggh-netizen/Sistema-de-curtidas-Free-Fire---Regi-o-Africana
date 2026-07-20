package com.freefire.curtidas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "free_fire_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreeFireAccount {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String ffAccountId;

    @Column(nullable = false)
    private String ffUsername;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer level;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer experience;

    private LocalDateTime createdAtFf;

    private LocalDateTime accountCreatedDate;

    private LocalDateTime lastSynced;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        lastSynced = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

package com.freefire.curtidas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountryCode country;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "plan_type DEFAULT 'BASICO'")
    private PlanType plan;

    @Column(unique = true)
    private String apiKey;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVerified;

    private LocalDateTime lastLogin;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        isActive = true;
        isVerified = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

package com.freefire.curtidas.controller;

import com.freefire.curtidas.dto.GuildInfoResponse;
import com.freefire.curtidas.service.GuildService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/guilda")
@RequiredArgsConstructor
@Tag(name = "Guilda", description = "Endpoints para gerenciar guildas")
@SecurityRequirement(name = "Bearer Authentication")
public class GuildController {

    private final GuildService guildService;

    @GetMapping("/{guildId}")
    @Operation(summary = "Obter informações da guilda")
    public ResponseEntity<GuildInfoResponse> getGuildInfo(@PathVariable String guildId) {
        GuildInfoResponse response = guildService.getGuildInfo(guildId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @Operation(summary = "Criar nova guilda")
    public ResponseEntity<GuildInfoResponse> createGuild(
            @RequestParam String guildId,
            @RequestParam String guildName,
            @RequestParam String leaderFfId) {
        var guild = guildService.createGuild(guildId, guildName, leaderFfId);
        GuildInfoResponse response = GuildInfoResponse.builder()
                .id(guild.getId())
                .guildId(guild.getGuildId())
                .guildName(guild.getGuildName())
                .level(guild.getLevel())
                .totalMembers(guild.getTotalMembers())
                .leaderFfId(guild.getLeaderFfId())
                .totalLikesReceived(guild.getTotalLikesReceived())
                .createdAt(guild.getCreatedAt())
                .build();
        return ResponseEntity.ok(response);
    }
}

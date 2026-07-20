package com.freefire.curtidas.service;

import com.freefire.curtidas.dto.GuildInfoResponse;
import com.freefire.curtidas.entity.Guild;
import com.freefire.curtidas.repository.GuildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class GuildService {

    private final GuildRepository guildRepository;

    public GuildInfoResponse getGuildInfo(String guildId) {
        Guild guild = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new IllegalArgumentException("Guilda não encontrada"));

        return mapToGuildInfoResponse(guild);
    }

    public Guild createGuild(String guildId, String guildName, String leaderFfId) {
        Guild guild = Guild.builder()
                .guildId(guildId)
                .guildName(guildName)
                .leaderFfId(leaderFfId)
                .level(1)
                .totalMembers(1)
                .experiencePoints(0)
                .totalLikesReceived(0)
                .build();

        return guildRepository.save(guild);
    }

    public void incrementGuildLikes(String guildId, Integer quantity) {
        Guild guild = guildRepository.findByGuildId(guildId)
                .orElseThrow(() -> new IllegalArgumentException("Guilda não encontrada"));

        guild.setTotalLikesReceived(guild.getTotalLikesReceived() + quantity);
        
        // Aumentar nível a cada 1000 likes
        if (guild.getTotalLikesReceived() % 1000 == 0) {
            guild.setLevel(guild.getLevel() + 1);
        }

        guildRepository.save(guild);
    }

    private GuildInfoResponse mapToGuildInfoResponse(Guild guild) {
        return GuildInfoResponse.builder()
                .id(guild.getId())
                .guildId(guild.getGuildId())
                .guildName(guild.getGuildName())
                .level(guild.getLevel())
                .totalMembers(guild.getTotalMembers())
                .leaderFfId(guild.getLeaderFfId())
                .country(guild.getCountry() != null ? guild.getCountry().toString() : null)
                .experiencePoints(guild.getExperiencePoints())
                .totalLikesReceived(guild.getTotalLikesReceived())
                .description(guild.getDescription())
                .createdAt(guild.getCreatedAt())
                .build();
    }
}

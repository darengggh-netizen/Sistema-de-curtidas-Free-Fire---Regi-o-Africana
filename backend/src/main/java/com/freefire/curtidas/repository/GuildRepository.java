package com.freefire.curtidas.repository;

import com.freefire.curtidas.entity.Guild;
import com.freefire.curtidas.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface GuildRepository extends JpaRepository<Guild, UUID> {
    Optional<Guild> findByGuildId(String guildId);
    List<Guild> findByCountry(CountryCode country);
    List<Guild> findByLeaderFfId(String leaderFfId);
}

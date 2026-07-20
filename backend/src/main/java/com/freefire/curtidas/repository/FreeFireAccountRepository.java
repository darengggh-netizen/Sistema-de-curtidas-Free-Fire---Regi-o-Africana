package com.freefire.curtidas.repository;

import com.freefire.curtidas.entity.FreeFireAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface FreeFireAccountRepository extends JpaRepository<FreeFireAccount, UUID> {
    List<FreeFireAccount> findByUserId(UUID userId);
    Optional<FreeFireAccount> findByFfAccountId(String ffAccountId);
    Optional<FreeFireAccount> findByUserIdAndFfAccountId(UUID userId, String ffAccountId);
}

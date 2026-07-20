package com.freefire.curtidas.repository;

import com.freefire.curtidas.entity.BooyahPass;
import com.freefire.curtidas.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Repository
public interface BooyahPassRepository extends JpaRepository<BooyahPass, UUID> {
    List<BooyahPass> findBySenderAccountId(UUID senderAccountId);
    List<BooyahPass> findByReceiverFfAccountId(String receiverFfAccountId);
    List<BooyahPass> findByStatus(TransactionStatus status);
    List<BooyahPass> findBySenderAccountIdAndCreatedAtAfter(UUID senderAccountId, LocalDateTime createdAt);
}

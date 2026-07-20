package com.freefire.curtidas.repository;

import com.freefire.curtidas.entity.Like;
import com.freefire.curtidas.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    List<Like> findBySenderAccountId(UUID senderAccountId);
    List<Like> findByReceiverFfAccountId(String receiverFfAccountId);
    List<Like> findByStatus(TransactionStatus status);
    List<Like> findBySenderAccountIdAndCreatedAtAfter(UUID senderAccountId, LocalDateTime createdAt);
}

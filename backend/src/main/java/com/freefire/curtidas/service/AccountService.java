package com.freefire.curtidas.service;

import com.freefire.curtidas.dto.AccountInfoResponse;
import com.freefire.curtidas.entity.FreeFireAccount;
import com.freefire.curtidas.entity.User;
import com.freefire.curtidas.repository.FreeFireAccountRepository;
import com.freefire.curtidas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final FreeFireAccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountInfoResponse addAccount(UUID userId, String ffAccountId, String ffUsername) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (accountRepository.findByUserIdAndFfAccountId(userId, ffAccountId).isPresent()) {
            throw new IllegalArgumentException("Conta já vinculada");
        }

        FreeFireAccount account = FreeFireAccount.builder()
                .user(user)
                .ffAccountId(ffAccountId)
                .ffUsername(ffUsername)
                .level(0)
                .experience(0)
                .accountCreatedDate(LocalDateTime.now())
                .build();

        FreeFireAccount saved = accountRepository.save(account);
        return mapToAccountInfoResponse(saved);
    }

    public AccountInfoResponse getAccountInfo(UUID accountId) {
        FreeFireAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        return mapToAccountInfoResponse(account);
    }

    public List<AccountInfoResponse> getUserAccounts(UUID userId) {
        return accountRepository.findByUserId(userId)
                .stream()
                .map(this::mapToAccountInfoResponse)
                .collect(Collectors.toList());
    }

    public LocalDateTime getAccountCreatedDate(String ffAccountId) {
        FreeFireAccount account = accountRepository.findByFfAccountId(ffAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Conta Free Fire não encontrada"));

        return account.getAccountCreatedDate();
    }

    private AccountInfoResponse mapToAccountInfoResponse(FreeFireAccount account) {
        return AccountInfoResponse.builder()
                .id(account.getId())
                .ffAccountId(account.getFfAccountId())
                .ffUsername(account.getFfUsername())
                .level(account.getLevel())
                .experience(account.getExperience())
                .accountCreatedDate(account.getAccountCreatedDate())
                .lastSynced(account.getLastSynced())
                .build();
    }
}

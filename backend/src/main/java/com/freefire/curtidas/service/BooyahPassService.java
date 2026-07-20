package com.freefire.curtidas.service;

import com.freefire.curtidas.entity.BooyahPass;
import com.freefire.curtidas.entity.FreeFireAccount;
import com.freefire.curtidas.entity.TransactionStatus;
import com.freefire.curtidas.repository.BooyahPassRepository;
import com.freefire.curtidas.repository.FreeFireAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BooyahPassService {

    private final BooyahPassRepository booyahPassRepository;
    private final FreeFireAccountRepository accountRepository;

    public BooyahPass sendBooyahPass(UUID userId, UUID accountId, String receiverFfAccountId, Integer quantity) {
        FreeFireAccount senderAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        if (!senderAccount.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Acesso negado");
        }

        BooyahPass booyahPass = BooyahPass.builder()
                .senderAccount(senderAccount)
                .receiverFfAccountId(receiverFfAccountId)
                .quantity(quantity)
                .status(TransactionStatus.PENDING)
                .build();

        BooyahPass saved = booyahPassRepository.save(booyahPass);

        // Simular envio
        simulateBooyahPassSend(saved);

        return saved;
    }

    private void simulateBooyahPassSend(BooyahPass booyahPass) {
        booyahPass.setStatus(TransactionStatus.SUCCESS);
        booyahPass.setResponseMessage("Booyah Pass enviado com sucesso!");
        booyahPassRepository.save(booyahPass);
    }
}

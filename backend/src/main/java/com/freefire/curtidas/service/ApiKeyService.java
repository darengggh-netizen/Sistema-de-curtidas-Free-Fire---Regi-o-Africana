package com.freefire.curtidas.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ApiKeyService {

    @Value("${api.key.salt}")
    private String apiKeySalt;

    public String generateApiKey(String email) {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String randomPart = Base64.getEncoder().encodeToString(randomBytes);
        String emailPart = email.split("@")[0];
        return (emailPart + "_" + randomPart).replaceAll("[^a-zA-Z0-9_-]", "").substring(0, 50);
    }

    public boolean validateApiKey(String apiKey) {
        return apiKey != null && apiKey.length() >= 20;
    }
}

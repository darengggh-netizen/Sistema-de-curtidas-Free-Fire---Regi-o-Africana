package com.freefire.curtidas.service;

import com.freefire.curtidas.config.JwtTokenProvider;
import com.freefire.curtidas.dto.AuthLoginRequest;
import com.freefire.curtidas.dto.AuthLoginResponse;
import com.freefire.curtidas.entity.User;
import com.freefire.curtidas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApiKeyService apiKeyService;

    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        if (!user.getIsActive()) {
            throw new IllegalArgumentException("Conta desativada");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Senha incorreta");
        }

        if (!user.getCountry().equals(request.getCountry())) {
            throw new IllegalArgumentException("País não corresponde");
        }

        String accessToken = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getCountry().toString(),
                user.getPlan().toString()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);

        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .plan(user.getPlan().toString())
                .country(user.getCountry().toString())
                .apiKey(user.getApiKey())
                .build();
    }

    public AuthLoginResponse register(AuthLoginRequest request, String username) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já registrado");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username já registrado");
        }

        String apiKey = apiKeyService.generateApiKey(request.getEmail());

        User user = User.builder()
                .email(request.getEmail())
                .username(username)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .country(request.getCountry())
                .apiKey(apiKey)
                .isActive(true)
                .isVerified(false)
                .build();

        userRepository.save(user);

        return AuthLoginResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .plan(user.getPlan().toString())
                .country(user.getCountry().toString())
                .apiKey(user.getApiKey())
                .build();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}

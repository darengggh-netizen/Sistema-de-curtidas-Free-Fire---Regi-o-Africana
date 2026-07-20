package com.freefire.curtidas.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginResponse {

    private String accessToken;
    private String refreshToken;
    private String email;
    private String username;
    private String plan;
    private String country;
    private String apiKey;
}

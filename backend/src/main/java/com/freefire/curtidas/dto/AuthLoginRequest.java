package com.freefire.curtidas.dto;

import com.freefire.curtidas.entity.CountryCode;
import com.freefire.curtidas.entity.PlanType;
import lombok.*;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRequest {

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotNull(message = "País é obrigatório")
    private CountryCode country;
}

package com.freefire.curtidas.dto;

import com.freefire.curtidas.entity.PlanType;
import lombok.*;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeRequest {

    @NotBlank(message = "ID da conta receptora é obrigatório")
    private String receiverFfAccountId;

    @Positive(message = "Quantidade deve ser maior que 0")
    private Integer quantity;

    @NotNull(message = "Plano é obrigatório")
    private PlanType plan;
}

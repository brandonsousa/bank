package br.com.exactaworks.exactabank.api.request.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record RechargeStoreRequest(
        @NotNull(message = "O campo 'contaOrigem' é obrigatório")
        Integer contaOrigem,
        @NotBlank(message = "O campo 'numeroTelefone' é obrigatório")
        String numeroTelefone,
        @NotNull(message = "O campo 'valor' é obrigatório")
        BigDecimal valor
) {
}

package io.github.brandonsousa.bank.api.request.transaction;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawStoreRequest(
        @NotNull(message = "O campo 'contaOrigem' é obrigatório")
        Integer contaOrigem,
        @NotNull(message = "O campo 'valor' é obrigatório")
        BigDecimal valor
) {
}

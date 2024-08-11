package io.github.brandonsousa.bank.api.request.transaction;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositStoreRequest(@NotNull(message = "O campo 'idConta' é obrigatório")
                                  Integer idConta,
                                  @NotNull(message = "O campo 'valor' é obrigatório")
                                  BigDecimal valor) {
}

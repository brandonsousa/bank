package io.github.brandonsousa.bank.api.response.transaction;

import io.github.brandonsousa.bank.api.serializer.BankAccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.UUID;

public record WithdrawStoreResponse(
        UUID id,
        @JsonSerialize(using = BankAccountSerializer.class)
        String contaOrigem,
        BigDecimal valor,
        BigDecimal saldoContaOrigem
) {
}

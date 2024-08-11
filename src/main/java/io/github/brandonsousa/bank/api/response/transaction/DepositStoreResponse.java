package io.github.brandonsousa.bank.api.response.transaction;

import io.github.brandonsousa.bank.api.serializer.BankAccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositStoreResponse(
        UUID id,
        @JsonSerialize(using = BankAccountSerializer.class)
        String conta,
        BigDecimal saldo
) {
}

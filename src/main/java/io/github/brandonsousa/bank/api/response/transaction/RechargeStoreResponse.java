package io.github.brandonsousa.bank.api.response.transaction;

import io.github.brandonsousa.bank.api.serializer.BankAccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.UUID;

public record RechargeStoreResponse(
        UUID id,
        @JsonSerialize(using = BankAccountSerializer.class)
        String contaOrigem,
        String numeroTelefone,
        String valor
) {
    @Override
    public String valor() {
        return new BigDecimal(valor).abs().toString();
    }
}

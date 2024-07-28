package br.com.exactaworks.exactabank.api.response.transaction;

import br.com.exactaworks.exactabank.api.serializer.BankAccountSerializer;
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

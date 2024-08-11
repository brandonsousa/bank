package io.github.brandonsousa.bank.api.response.banckAccount;

import io.github.brandonsousa.bank.api.serializer.BankAccountDocumentSerializer;
import io.github.brandonsousa.bank.api.serializer.BankAccountSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;

public record BankAccountListResponse(
        String nome,
        @JsonSerialize(using = BankAccountDocumentSerializer.class)
        String documento,
        @JsonSerialize(using = BankAccountSerializer.class)
        String conta,
        BigDecimal saldo) {
}

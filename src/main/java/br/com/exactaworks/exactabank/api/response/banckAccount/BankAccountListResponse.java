package br.com.exactaworks.exactabank.api.response.banckAccount;

import br.com.exactaworks.exactabank.api.serializer.BankAccountDocumentSerializer;
import br.com.exactaworks.exactabank.api.serializer.BankAccountSerializer;
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

package io.github.brandonsousa.bank.api.request.transaction;

import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PixStoreRequest(
        @NotNull(message = "O campo 'contaOrigem' é obrigatório")
        Integer contaOrigem,
        @NotNull(message = "O campo 'tipoChavePix' é obrigatório")
        TpChavePixEnum tipoChavePix,
        @NotBlank(message = "O campo 'chavePixDestino' é obrigatório")
        String chavePixDestino,
        @NotNull(message = "O campo 'valor' é obrigatório")
        BigDecimal valor
) {

}

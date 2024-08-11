package io.github.brandonsousa.bank.api.request.pix;

import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import jakarta.validation.constraints.NotNull;

public record PixStoreRequest(
        @NotNull(message = "O campo 'idConta' é obrigatório")
        Integer idConta,
        @NotNull(message = "O campo 'tpChave' é obrigatório")
        TpChavePixEnum tpChave,
        String chave
) {
}

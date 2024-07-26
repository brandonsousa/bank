package br.com.exactaworks.exactabank.api.request.pix;

import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import jakarta.validation.constraints.NotNull;

public record PixStoreRequest(
        @NotNull(message = "O campo 'idConta' é obrigatório")
        Integer idConta,
        @NotNull(message = "O campo 'tpChave' é obrigatório")
        TpChavePixEnum tpChave,
        String chave
) {
}

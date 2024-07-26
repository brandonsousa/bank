package br.com.exactaworks.exactabank.api.response.pix;

import br.com.exactaworks.exactabank.api.serializer.BankAccountSerializer;
import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record PixStoreResponse(
        TpChavePixEnum tpChave,
        String chave,
        @JsonSerialize(using = BankAccountSerializer.class)
        String conta
) {

}

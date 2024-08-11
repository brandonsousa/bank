package io.github.brandonsousa.bank.api.response.pix;

import io.github.brandonsousa.bank.api.serializer.BankAccountSerializer;
import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public record PixStoreResponse(
        TpChavePixEnum tpChave,
        String chave,
        @JsonSerialize(using = BankAccountSerializer.class)
        String conta
) {

}

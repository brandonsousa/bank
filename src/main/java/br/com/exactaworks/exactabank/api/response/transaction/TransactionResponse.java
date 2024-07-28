package br.com.exactaworks.exactabank.api.response.transaction;

import br.com.exactaworks.exactabank.api.serializer.BankAccountSerializer;
import br.com.exactaworks.exactabank.enums.TpTransacaoEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
    private UUID id;
    @JsonSerialize(using = BankAccountSerializer.class)
    private String contaOrigem;
    private String usuarioOrigem;
    private String chavePixDestino;
    private String usuarioDestino;
    @JsonSerialize(using = BankAccountSerializer.class)
    private String contaDestino;
    private TpTransacaoEnum tpTransacao;
    private BigDecimal valor;
    private LocalDateTime dtCriacao;

    public void adjustValueToOwner(Integer accountId) {
        Objects.requireNonNull(accountId, "Necessário informar o id da conta");
        if (Objects.equals(tpTransacao, TpTransacaoEnum.PIX) && Objects.equals(contaDestino,
                String.valueOf(accountId))) {
            this.valor = valor.abs();
        }
    }
}

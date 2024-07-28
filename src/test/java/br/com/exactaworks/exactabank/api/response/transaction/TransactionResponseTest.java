package br.com.exactaworks.exactabank.api.response.transaction;

import br.com.exactaworks.exactabank.enums.TpTransacaoEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TransactionResponseTest {
    @Nested
    class AdjustValueToOwner {
        @Nested
        @DisplayName("Should adjust value to owner")
        class Success {
            @Test
            @DisplayName("When transaction type is PIX and destination account is the same as the owner should adjust" +
                    " value to positive")
            void whenTransactionTypeIsPixAndDestinationAccountIsTheSameAsTheOwnerShouldAdjustValueToPositive() {
                TransactionResponse response = new TransactionResponse();
                response.setTpTransacao(TpTransacaoEnum.PIX);
                response.setContaDestino("1");
                response.setValor(BigDecimal.ONE.negate());

                response.adjustValueToOwner(1);

                assertEquals(BigDecimal.ONE, response.getValor());
            }

            @Test
            @DisplayName("When transaction type is not PIX should not adjust value")
            void whenTransactionTypeIsNotPixShouldNotAdjustValue() {
                TransactionResponse response = new TransactionResponse();
                response.setTpTransacao(TpTransacaoEnum.DEPOSITO);
                response.setContaDestino("1");
                response.setValor(BigDecimal.ONE.negate());

                response.adjustValueToOwner(1);

                assertEquals(BigDecimal.ONE.negate(), response.getValor());
            }

            @Test
            @DisplayName("When transaction type is PIX and destination account is not the same as the owner should " +
                    "not adjust" +
                    " value")
            void whenTransactionTypeIsPixAndDestinationAccountIsNotTheSameAsTheOwnerShouldNotAdjustValue() {
                TransactionResponse response = new TransactionResponse();
                response.setTpTransacao(TpTransacaoEnum.PIX);
                response.setContaDestino("2");
                response.setValor(BigDecimal.ONE.negate());

                response.adjustValueToOwner(1);

                assertEquals(BigDecimal.ONE.negate(), response.getValor());
            }
        }
    }
}
package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountStoreResponse;
import br.com.exactaworks.exactabank.api.response.util.BankAccountUtil;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class BankAccountMapperTest {
    private BankAccountMapper MAPPER = BankAccountMapper.INSTANCE;

    @Nested
    @DisplayName("When convert ContaEntity to BankAccountStoreResponse")
    class FromContaEntity {
        @Nested
        @DisplayName("Should convert")
        class Success {
            @Test
            @DisplayName("Then return BankAccountStoreResponse")
            void thenReturnBankAccountStoreResponse() {
                ContaEntity entity = ContaEntity.builder()
                        .conta(new Random().nextInt())
                        .nome("Test")
                        .documento("12345678901")
                        .saldo(BigDecimal.TEN).build();

                BankAccountStoreResponse response = MAPPER.fromContaEntity(entity);

                assertEquals(entity.getNome(), response.nome());
                assertEquals(BankAccountUtil.formatDocument(entity.getDocumento()), response.documento());
                assertEquals(BankAccountUtil.formatAccountNumber(entity.getConta()), response.conta());
                assertEquals(entity.getSaldo(), response.saldo());
            }

            @Test
            @DisplayName("Then return null when entity is null")
            void thenReturnNullWhenEntityIsNull() {
                BankAccountStoreResponse response = MAPPER.fromContaEntity(null);

                assertNull(response);
            }
        }
    }
}
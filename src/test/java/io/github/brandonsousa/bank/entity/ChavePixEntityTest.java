package io.github.brandonsousa.bank.entity;

import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ChavePixEntityTest {
    @Nested
    class Create {
        @Nested
        @DisplayName("Create key successfully")
        class Success {
            private final ContaEntity entity = ContaEntity.builder()
                    .id(UUID.randomUUID())
                    .conta(new Random().nextInt())
                    .nome("Test")
                    .documento("12345678901")
                    .saldo(BigDecimal.TEN).build();

            @ParameterizedTest(name = "when {0}")
            @EnumSource(value = TpChavePixEnum.class, mode = EnumSource.Mode.INCLUDE, names = {"CPF", "TELEFONE" })
            void createKeySuccessfully(TpChavePixEnum keyType) {
                ChavePixEntity key = ChavePixEntity.create(entity, keyType, "83923422083");

                assertEquals(entity.getId(), key.getIdConta());
                assertEquals(keyType, key.getTpChave());
                assertEquals("83923422083", key.getChave());
            }

            @Test
            @DisplayName("Create random key successfully")
            void createRandomKeySuccessfully() {
                ChavePixEntity key = ChavePixEntity.create(entity, TpChavePixEnum.ALEATORIA, "83923422083");

                assertEquals(entity.getId(), key.getIdConta());
                assertEquals(TpChavePixEnum.ALEATORIA, key.getTpChave());
                assertNotEquals("83923422083", key.getChave());
                assertFalse(key.getChave().isBlank());
            }

            @Test
            @DisplayName("Create email key successfully")
            void createEmailKeySuccessfully() {
                String email = "email@emial.com";

                ChavePixEntity key = ChavePixEntity.create(entity, TpChavePixEnum.EMAIL, email);

                assertEquals(entity.getId(), key.getIdConta());
                assertEquals(TpChavePixEnum.EMAIL, key.getTpChave());
                assertEquals(email, key.getChave());
            }
        }

        @Nested
        @DisplayName("Fail to create key")
        class Fail {
            @Test
            @DisplayName("Throws NullPointerException when account is null")
            void throwsNullPointerExceptionWhenAccountIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ChavePixEntity.create(null, TpChavePixEnum.CPF, "83923422083"));

                String expectedMessage = "Conta não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws NullPointerException when key type is null")
            void throwsNullPointerExceptionWhenKeyTypeIsNull() {
                ContaEntity bankAccount = new ContaEntity();
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ChavePixEntity.create(bankAccount, null, "83923422083"));

                String expectedMessage = "Tipo de chave não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws NullPointerException when key is null")
            void throwsNullPointerExceptionWhenKeyIsNull() {
                ContaEntity bankAccount = new ContaEntity();
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ChavePixEntity.create(bankAccount, TpChavePixEnum.CPF, null));

                String expectedMessage = "Chave não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when key is blank")
            void throwsIllegalArgumentExceptionWhenKeyIsBlank() {
                ContaEntity bankAccount = new ContaEntity();
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> ChavePixEntity.create(bankAccount, TpChavePixEnum.CPF, ""));

                String expectedMessage = "Chave não pode ser vazia";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
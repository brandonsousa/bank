package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.enums.TpTransacaoEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransacaoEntityTest {
    @Nested
    class Create {
        @Nested
        class Success {
            @ParameterizedTest(name = "{0}")
            @EnumSource(value = TpTransacaoEnum.class, mode = EnumSource.Mode.EXCLUDE, names = {"DEPOSITO"})
            @DisplayName("Should create transaction successfully")
            void shouldCreateTransactionSuccessfully(TpTransacaoEnum type) {
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                TransacaoEntity transaction = TransacaoEntity.create(type, value, description, originAccount,
                        pixDestiny, destinyAccount);

                assertEquals(type, transaction.getTpTransacao());
                assertEquals(value.negate(), transaction.getValor());
                assertEquals(description, transaction.getDescricao());
                assertEquals(originAccount.getId(), transaction.getIdContaOrigem());
                assertEquals(pixDestiny.getId(), transaction.getIdChavePixDestino());
                assertEquals(destinyAccount.getId(), transaction.getIdContaDestino());
                assertEquals(originAccount, transaction.getContaOrigem());
                assertEquals(pixDestiny, transaction.getChavePixDestino());
                assertEquals(destinyAccount, transaction.getContaDestino());
            }


            @Test
            @DisplayName("Should create transaction successfully when is a DEPOSITO")
            void shouldCreateTransactionSuccessfullyWhenIsADeposit() {
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                TransacaoEntity transaction = TransacaoEntity.create(TpTransacaoEnum.DEPOSITO, value, description,
                        originAccount, pixDestiny, destinyAccount);

                assertEquals(TpTransacaoEnum.DEPOSITO, transaction.getTpTransacao());
                assertEquals(value, transaction.getValor());
                assertEquals(description, transaction.getDescricao());
                assertEquals(originAccount.getId(), transaction.getIdContaOrigem());
                assertEquals(pixDestiny.getId(), transaction.getIdChavePixDestino());
                assertEquals(destinyAccount.getId(), transaction.getIdContaDestino());
                assertEquals(originAccount, transaction.getContaOrigem());
                assertEquals(pixDestiny, transaction.getChavePixDestino());
                assertEquals(destinyAccount, transaction.getContaDestino());
            }
        }

        @Nested
        @DisplayName("Fail to create a transaction")
        class Fail {
            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is equal to zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsEqualToZero() {
                TpTransacaoEnum type = TpTransacaoEnum.DEPOSITO;
                BigDecimal value = BigDecimal.ZERO;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.create(type, value, description, originAccount, pixDestiny,
                                destinyAccount));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is less than zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsLessThanZero() {
                TpTransacaoEnum type = TpTransacaoEnum.DEPOSITO;
                BigDecimal value = BigDecimal.valueOf(-1);
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.create(type, value, description, originAccount, pixDestiny,
                                destinyAccount));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsNull() {
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(null, value, description, originAccount, pixDestiny,
                                destinyAccount));

                String expectedMessage = "Tipo de transação não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value of transaction is null")
            void shouldThrowNullPointerExceptionWhenValueOfTransactionIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.DEPOSITO;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, null, description, originAccount, pixDestiny,
                                destinyAccount));

                String expectedMessage = "Valor da transação não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is PIX and origin account is null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsPixAndOriginAccountIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.PIX;
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, value, description, null, pixDestiny, destinyAccount));

                String expectedMessage = "Conta de origem não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is PIX and pix destiny is null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsPixAndPixDestinyIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.PIX;
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, value, description, originAccount, null, destinyAccount));

                String expectedMessage = "Chave PIX destino não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is SAQUE and origin account is " +
                    "null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsSaqueAndOriginAccountIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.SAQUE;
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, value, description, null, pixDestiny, destinyAccount));

                String expectedMessage = "Conta origem não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is RECARGA and origin account is" +
                    " " +
                    "null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsRecargaAndOriginAccountIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.RECARGA;
                BigDecimal value = BigDecimal.TEN;
                String description = "description";
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, value, description, null, pixDestiny, destinyAccount));

                String expectedMessage = "Conta origem não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when type of transaction is RECARGA and description is " +
                    "null")
            void shouldThrowNullPointerExceptionWhenTypeOfTransactionIsRecargaAndDescriptionIsNull() {
                TpTransacaoEnum type = TpTransacaoEnum.RECARGA;
                BigDecimal value = BigDecimal.TEN;
                ContaEntity originAccount = new ContaEntity();
                ChavePixEntity pixDestiny = new ChavePixEntity();
                ContaEntity destinyAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.create(type, value, null, originAccount, pixDestiny, destinyAccount));

                String expectedMessage = "Número de telefone não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class Deposit {
        @Nested
        @DisplayName("Success to create a deposit transaction")
        class Success {
            @Test
            @DisplayName("Should create deposit transaction successfully")
            void shouldCreateDepositTransactionSuccessfully() {
                ContaEntity account = new ContaEntity();
                BigDecimal value = BigDecimal.TEN;

                TransacaoEntity transaction = TransacaoEntity.deposit(account, value);

                assertEquals(TpTransacaoEnum.DEPOSITO, transaction.getTpTransacao());
                assertEquals(value, transaction.getValor());
                assertNull(transaction.getDescricao());
                assertNull(transaction.getIdContaOrigem());
                assertNull(transaction.getIdChavePixDestino());
                assertEquals(account.getId(), transaction.getIdContaDestino());
                assertNull(transaction.getContaOrigem());
                assertNull(transaction.getChavePixDestino());
                assertEquals(account, transaction.getContaDestino());
            }
        }

        @Nested
        @DisplayName("Fail to create a deposit transaction")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when account is null")
            void shouldThrowNullPointerExceptionWhenAccountIsNull() {
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.deposit(null, value));

                String expectedMessage = "Conta destino não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                ContaEntity account = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.deposit(account, null));

                String expectedMessage = "Valor da transação não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class Pix {
        @Nested
        @DisplayName("Success to create a PIX transaction")
        class Success {
            @Test
            @DisplayName("Should create PIX transaction successfully")
            void shouldCreatePixTransactionSuccessfully() {
                BigDecimal value = BigDecimal.TEN;
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = ContaEntity.builder().id(UUID.randomUUID()).build();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                TransacaoEntity transaction = TransacaoEntity.pix(originAccount, pixDestiny, value);

                assertEquals(TpTransacaoEnum.PIX, transaction.getTpTransacao());
                assertEquals(value.negate(), transaction.getValor());
                assertNull(transaction.getDescricao());
                assertEquals(originAccount.getId(), transaction.getIdContaOrigem());
                assertEquals(pixDestiny.getId(), transaction.getIdChavePixDestino());
                assertEquals(destinyAccount.getId(), transaction.getIdContaDestino());
                assertEquals(originAccount, transaction.getContaOrigem());
                assertEquals(pixDestiny, transaction.getChavePixDestino());
                assertEquals(destinyAccount, transaction.getContaDestino());
            }
        }

        @Nested
        @DisplayName("Fail to create a PIX transaction")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when origin account is null")
            void shouldThrowNullPointerExceptionWhenOriginAccountIsNull() {
                BigDecimal value = BigDecimal.TEN;
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.pix(null, pixDestiny, value));

                String expectedMessage = "Conta de origem não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when pix destiny is null")
            void shouldThrowNullPointerExceptionWhenPixDestinyIsNull() {
                BigDecimal value = BigDecimal.TEN;
                ContaEntity originAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.pix(originAccount, null, value));

                String expectedMessage = "Chave PIX destino não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.pix(originAccount, pixDestiny, null));

                String expectedMessage = "Valor da transação não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is equal to zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsEqualToZero() {
                BigDecimal value = BigDecimal.ZERO;
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.pix(originAccount, pixDestiny, value));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is less than zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsLessThanZero() {
                BigDecimal value = BigDecimal.valueOf(-1);
                ContaEntity originAccount = new ContaEntity();
                ContaEntity destinyAccount = new ContaEntity();
                ChavePixEntity pixDestiny = ChavePixEntity.builder().conta(destinyAccount).build();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.pix(originAccount, pixDestiny, value));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class Withdraw {
        @Nested
        @DisplayName("Success to create a SAQUE transaction")
        class Success {
            @Test
            @DisplayName("Should create SAQUE transaction successfully")
            void shouldCreateWithdrawTransactionSuccessfully() {
                BigDecimal value = BigDecimal.TEN;
                ContaEntity originAccount = ContaEntity.builder().id(UUID.randomUUID()).build();

                TransacaoEntity transaction = TransacaoEntity.withdraw(originAccount, value);

                assertEquals(TpTransacaoEnum.SAQUE, transaction.getTpTransacao());
                assertEquals(value.negate(), transaction.getValor());
                assertNull(transaction.getDescricao());
                assertEquals(originAccount.getId(), transaction.getIdContaOrigem());
                assertNull(transaction.getIdChavePixDestino());
                assertNull(transaction.getIdContaDestino());
                assertEquals(originAccount, transaction.getContaOrigem());
                assertNull(transaction.getChavePixDestino());
                assertNull(transaction.getContaDestino());
            }
        }

        @Nested
        @DisplayName("Fail to create a SAQUE transaction")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when origin account is null")
            void shouldThrowNullPointerExceptionWhenOriginAccountIsNull() {
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.withdraw(null, value));

                String expectedMessage = "Conta origem não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                ContaEntity originAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> TransacaoEntity.withdraw(originAccount, null));

                String expectedMessage = "Valor da transação não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is equal to zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsEqualToZero() {
                BigDecimal value = BigDecimal.ZERO;
                ContaEntity originAccount = new ContaEntity();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.withdraw(originAccount, value));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw IllegalArgumentException when value of transaction is less than zero")
            void shouldThrowIllegalArgumentExceptionWhenValueOfTransactionIsLessThanZero() {
                BigDecimal value = BigDecimal.valueOf(-1);
                ContaEntity originAccount = new ContaEntity();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> TransacaoEntity.withdraw(originAccount, value));

                String expectedMessage = "Valor da transação deve ser maior que zero";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
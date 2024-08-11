package io.github.brandonsousa.bank.entity;

import io.github.brandonsousa.bank.dvo.DocumentoDVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ContaEntityTest {
    @Nested
    class Create {
        @Nested
        @DisplayName("Create account successfully")
        class Success {
            @Test
            @DisplayName("Create account with valid name and document")
            void createContaWithValidNameAndDocument() {
                String name = "Nome";
                DocumentoDVO document = new DocumentoDVO("83923422083");
                Integer account = new Random().nextInt();

                ContaEntity bankAccount = ContaEntity.create(name, document, account);

                assertNotNull(bankAccount.getId());
                assertEquals(name, bankAccount.getNome());
                assertEquals(document.value(), bankAccount.getDocumento());
                assertEquals(account, bankAccount.getConta());
                assertEquals(BigDecimal.ZERO, bankAccount.getSaldo());
            }
        }

        @Nested
        @DisplayName("Fail to create account")
        class Fail {
            @Test
            @DisplayName("Throws NullPointerException when name is null")
            void throwsNullPointerExceptionWhenNomeIsNull() {
                DocumentoDVO document = new DocumentoDVO("83923422083");
                Integer account = new Random().nextInt();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ContaEntity.create(null, document, account));

                String expectedMessage = "Nome não pode ser nulo";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws NullPointerException when document is null")
            void throwsNullPointerExceptionWhenDocumentIsNull() {
                String name = "Nome";
                Integer account = new Random().nextInt();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ContaEntity.create(name, null, account));

                String expectedMessage = "Documento não pode ser nulo";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when name is empty")
            void throwsIllegalArgumentExceptionWhenNameIsEmpty() {
                String name = "";
                DocumentoDVO document = new DocumentoDVO("83923422083");
                Integer account = new Random().nextInt();

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> ContaEntity.create(name, document, account));

                String expectedMessage = "Nome não pode ser vazio";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when document is invalid")
            void throwsNullPointerExceptionWhenNrContaIsNull() {
                String name = "Nome";
                DocumentoDVO document = new DocumentoDVO("83923422083");

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> ContaEntity.create(name, document, null));

                String expectedMessage = "Conta não pode ser nulo";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class IncrementAmount {
        @Nested
        @DisplayName("Increment amount successfully")
        class Success {
            @Test
            @DisplayName("Increment amount with valid value")
            void incrementAmountWithValidValue() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.TEN;

                bankAccount.incrementAmount(value);

                assertEquals(value, bankAccount.getSaldo());
            }
        }

        @Nested
        class Fail {
            @Test
            @DisplayName("Throws NullPointerException when value is null")
            void throwsNullPointerExceptionWhenValueIsNull() {
                ContaEntity bankAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> bankAccount.incrementAmount(null));

                String expectedMessage = "Valor não pode ser nulo";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when value is negative")
            void throwsIllegalArgumentExceptionWhenValueIsNegative() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.valueOf(-10);

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> bankAccount.incrementAmount(value));

                String expectedMessage = "Valor deve ser maior que zero";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when value is zero")
            void throwsIllegalArgumentExceptionWhenValueIsZero() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.ZERO;

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> bankAccount.incrementAmount(value));

                String expectedMessage = "Valor deve ser maior que zero";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class DecrementAmount {
        @Nested
        @DisplayName("Decrement amount successfully")
        class Success {
            @Test
            @DisplayName("Decrement amount with valid value")
            void decrementAmountWithValidValue() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.TEN;
                bankAccount.incrementAmount(BigDecimal.valueOf(20));

                bankAccount.decrementAmount(value);

                assertEquals(BigDecimal.TEN, bankAccount.getSaldo());
            }
        }

        @Nested
        class Fail {
            @Test
            @DisplayName("Throws NullPointerException when value is null")
            void throwsNullPointerExceptionWhenValueIsNull() {
                ContaEntity bankAccount = new ContaEntity();

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> bankAccount.decrementAmount(null));

                String expectedMessage = "Valor não pode ser nulo";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when value is negative")
            void throwsIllegalArgumentExceptionWhenValueIsNegative() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.valueOf(-10);

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> bankAccount.decrementAmount(value));

                String expectedMessage = "Valor deve ser maior que zero";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when value is zero")
            void throwsIllegalArgumentExceptionWhenValueIsZero() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.ZERO;

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> bankAccount.decrementAmount(value));

                String expectedMessage = "Valor deve ser maior que zero";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Throws IllegalArgumentException when value is greater than saldo")
            void throwsIllegalArgumentExceptionWhenValueIsGreaterThanSaldo() {
                ContaEntity bankAccount = new ContaEntity();
                BigDecimal value = BigDecimal.TEN;

                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> bankAccount.decrementAmount(value));

                String expectedMessage = "Saldo insuficiente";

                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
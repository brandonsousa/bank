package io.github.brandonsousa.bank.api.response.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BankAccountUtilTest {
    @Nested
    class FormatAccountNumber {
        @Nested
        @DisplayName("When account number is valid")
        class Success {
            @Test
            @DisplayName("Then format account number")
            void formatAccountNumber() {
                int accountNumber = new Random().nextInt();

                String formattedAccountNumber = BankAccountUtil.formatAccountNumber(accountNumber);

                assertEquals(String.format("%010d", accountNumber), formattedAccountNumber);
            }
        }

        @Nested
        @DisplayName("Should fail")
        class Fail {
            @Test
            @DisplayName("When account number is null")
            void formatAccountNumber() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> BankAccountUtil.formatAccountNumber(null));

                String expectedMessage = "Conta não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class FormatDocument {
        @Nested
        @DisplayName("When document is valid")
        class Success {
            @Test
            @DisplayName("Should return formatted cpf")
            void formatDocumentCPF() {
                String document = "12345678901";

                String formattedDocument = BankAccountUtil.formatDocument(document);

                assertEquals("123.456.789-01", formattedDocument);
            }

            @Test
            @DisplayName("Should return formatted cnpj")
            void formatDocumentCNPJ() {
                String document = "12345678901234";

                String formattedDocument = BankAccountUtil.formatDocument(document);

                assertEquals("12.345.678/9012-34", formattedDocument);
            }
        }

        @Nested
        @DisplayName("When invalid document")
        class Fail {
            @Test
            @DisplayName("Should throw exception NullPointerException when document is null")
            void shouldThrowExceptionWhenDocumentIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> BankAccountUtil.formatDocument(null));

                String expectedMessage = "Documento não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw exception IllegalArgumentException when document has lass than 11 characters")
            void shouldThrowExceptionWhenDocumentHasLessThan11Characters() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> BankAccountUtil.formatDocument("1234567890"));

                String expectedMessage = "Documento inválido";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw exception IllegalArgumentException when document has more than 14 characters")
            void shouldThrowExceptionWhenDocumentHasMoreThan14Characters() {
                IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                        () -> BankAccountUtil.formatDocument("123456789012345"));

                String expectedMessage = "Documento inválido";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
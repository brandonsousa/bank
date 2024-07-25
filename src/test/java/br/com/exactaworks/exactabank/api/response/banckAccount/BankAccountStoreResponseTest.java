package br.com.exactaworks.exactabank.api.response.banckAccount;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BankAccountStoreResponseTest {
    @Nested
    @DisplayName("Constructor tests for BankAccountStoreResponse(String, String, String, BigDecimal)")
    class BankAccountStoreResponseConstructor {
        @Nested
        @DisplayName("Create with success")
        class Success {
            @Test
            @DisplayName("Should create a BankAccountStoreResponse successfully")
            void shouldCreateBankAccountStoreResponseWithSuccess() {
                BankAccountStoreResponse bankAccountStoreResponse = assertDoesNotThrow(
                        () -> new BankAccountStoreResponse("Nome", "12345678901", "123456", BigDecimal.TEN));

                assertEquals("Nome", bankAccountStoreResponse.nome());
                assertEquals("123.456.789-01", bankAccountStoreResponse.documento());
                assertEquals("0000123456", bankAccountStoreResponse.conta());
                assertEquals(BigDecimal.TEN, bankAccountStoreResponse.saldo());
            }
        }

        @Nested
        @DisplayName("Fail to create")
        class Fail {
            @Test
            @DisplayName("When name is null")
            void shouldFailToCreateBankAccountStoreResponseWhenNameIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> new BankAccountStoreResponse(null, "12345678901", "123456", BigDecimal.TEN));

                String expectedMessage = "O campo 'nome' é obrigatório";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When document is null")
            void shouldFailToCreateBankAccountStoreResponseWhenDocumentIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> new BankAccountStoreResponse("Nome", null, "123456", BigDecimal.TEN));

                String expectedMessage = "O campo 'documento' é obrigatório";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When account is null")
            void shouldFailToCreateBankAccountStoreResponseWhenAccountIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> new BankAccountStoreResponse("Nome", "12345678901", null, BigDecimal.TEN));

                String expectedMessage = "O campo 'conta' é obrigatório";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When balance is null")
            void shouldFailToCreateBankAccountStoreResponseWhenBalanceIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> new BankAccountStoreResponse("Nome", "12345678901", "123456", null));

                String expectedMessage = "O campo 'saldo' é obrigatório";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.dvo.DocumentoDVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
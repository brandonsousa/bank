package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.exception.BadRequestException;
import br.com.exactaworks.exactabank.repository.BankAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {
    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private BankAccountService service;

    @Nested
    class Store {
        @Nested
        @DisplayName("Store bank account successfully")
        class Success {
            @Test
            @DisplayName("Should store bank account with valid name and document")
            void shouldStoreBankAccountWithValidNameAndDocument() {
                String name = "Nome";
                String document = "83923422083";
                Integer account = new Random().nextInt();

                when(repository.existsByDocumento(document)).thenReturn(false);
                when(repository.getNextContaValue()).thenReturn(account);

                service.store(name, document);

                ArgumentCaptor<ContaEntity> captor = ArgumentCaptor.forClass(ContaEntity.class);
                verify(repository, times(1)).save(captor.capture());

                ContaEntity bankAccount = captor.getValue();

                assertNotNull(bankAccount.getId());
                assertEquals(name, bankAccount.getNome());
                assertEquals(document, bankAccount.getDocumento());
                assertEquals(account, bankAccount.getConta());
            }

            @Nested
            @DisplayName("Fail to store bank account")
            class Fail {
                @Test
                @DisplayName("Should throw NullPointerException when name is null")
                void shouldThrowNullPointerExceptionWhenNameIsNull() {
                    NullPointerException exception = assertThrows(NullPointerException.class,
                            () -> service.store(null, "83923422083"));

                    String expectedMessage = "Nome não pode ser nulo";

                    String actualMessage = exception.getMessage();

                    assertEquals(expectedMessage, actualMessage);
                }

                @Test
                @DisplayName("Should throw NullPointerException when document is null")
                void shouldThrowNullPointerExceptionWhenDocumentIsNull() {
                    NullPointerException exception = assertThrows(NullPointerException.class,
                            () -> service.store("Nome", null));

                    String expectedMessage = "Documento não pode ser nulo";

                    String actualMessage = exception.getMessage();

                    assertEquals(expectedMessage, actualMessage);
                }

                @Test
                @DisplayName("Should throw BadRequestException when bank account already exists")
                void shouldThrowBadRequestExceptionWhenBankAccountAlreadyExists() {
                    String name = "Nome";
                    String document = "83923422083";

                    when(repository.existsByDocumento(document)).thenReturn(true);

                    BadRequestException exception = assertThrows(BadRequestException.class,
                            () -> service.store(name, document));

                    String expectedMessage = "Já existe conta para o cliente";

                    String actualMessage = exception.getMessage();

                    assertEquals(expectedMessage, actualMessage);
                }

                @Test
                @DisplayName("Should throw NullPointerException when account is null")
                void shouldThrowNullPointerExceptionWhenAccountIsNull() {
                    String name = "Nome";
                    String document = "83923422083";

                    when(repository.existsByDocumento(document)).thenReturn(false);
                    when(repository.getNextContaValue()).thenReturn(null);

                    NullPointerException exception = assertThrows(NullPointerException.class,
                            () -> service.store(name, document));

                    String expectedMessage = "Conta não pode ser nula";

                    String actualMessage = exception.getMessage();

                    assertEquals(expectedMessage, actualMessage);
                }
            }
        }
    }
}
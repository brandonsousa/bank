package io.github.brandonsousa.bank.service;

import io.github.brandonsousa.bank.entity.ContaEntity;
import io.github.brandonsousa.bank.exception.BadRequestException;
import io.github.brandonsousa.bank.exception.NotFoundException;
import io.github.brandonsousa.bank.repository.BankAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
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

    @Nested
    class FindByConta {
        @Nested
        @DisplayName("Find bank account by account successfully")
        class Success {
            @Test
            @DisplayName("Should find bank account by account")
            void shouldFindBankAccountByAccount() {
                Integer account = new Random().nextInt();
                ContaEntity bankAccount = new ContaEntity();

                when(repository.findByConta(account)).thenReturn(java.util.Optional.of(bankAccount));

                ContaEntity result = service.findByConta(account);

                assertEquals(bankAccount, result);
            }
        }

        @Nested
        @DisplayName("Fail to find bank account")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when account is null")
            void shouldThrowNullPointerExceptionWhenAccountIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.findByConta(null));

                String expectedMessage = "Conta não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NotFoundException when bank account not found")
            void shouldThrowNotFoundExceptionWhenBankAccountNotFound() {
                Integer account = new Random().nextInt();

                when(repository.findByConta(account)).thenReturn(Optional.empty());

                NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findByConta(account));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class FindAll {
        @Nested
        class Success {
            @Test
            @DisplayName("Should find all bank accounts")
            void shouldFindAllBankAccounts() {
                int page = 0;
                int size = 10;
                String filter = "filter";

                Pageable pageable = PageRequest.of(page, size);

                service.findAll(page, size, filter);

                verify(repository, times(1)).findByFilter(filter, pageable);
            }
        }
    }

    @Nested
    class IncrementAmount {
        @Nested
        @DisplayName("Update balance successfully")
        class Success {
            @Test
            @DisplayName("Should update balance for account")
            void shouldUpdateBalanceForAccount() {
                Integer accountId = new Random().nextInt();
                ContaEntity account = new ContaEntity();
                BigDecimal value = BigDecimal.TEN;

                when(repository.findByConta(accountId)).thenReturn(Optional.of(account));

                service.incrementAmount(accountId, value);

                ArgumentCaptor<ContaEntity> captor = ArgumentCaptor.forClass(ContaEntity.class);
                verify(repository, times(1)).save(captor.capture());

                ContaEntity updatedAccount = captor.getValue();

                assertEquals(value, updatedAccount.getSaldo());
            }
        }

        @Nested
        @DisplayName("Fail to update balance")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when account id is null")
            void shouldThrowNullPointerExceptionWhenAccountIdIsNull() {
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.incrementAmount(null, value));

                String expectedMessage = "Necessário informar o id da conta";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                int accountId = new Random().nextInt();
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.incrementAmount(accountId, null));

                String expectedMessage = "Necessário informar o valor da transação";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NotFoundException when bank account not found")
            void shouldThrowNotFoundExceptionWhenBankAccountNotFound() {
                Integer accountId = new Random().nextInt();
                BigDecimal value = BigDecimal.TEN;

                when(repository.findByConta(accountId)).thenReturn(Optional.empty());

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.incrementAmount(accountId, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class DecreaseAmount {
        @Nested
        @DisplayName("Update balance successfully")
        class Success {
            @Test
            @DisplayName("Should update balance for account")
            void shouldUpdateBalanceForAccount() {
                Integer accountId = new Random().nextInt();
                ContaEntity account = ContaEntity.builder().saldo(BigDecimal.TEN).build();
                BigDecimal value = BigDecimal.TEN;

                when(repository.findByConta(accountId)).thenReturn(Optional.of(account));

                service.decrementAmount(accountId, value);

                ArgumentCaptor<ContaEntity> captor = ArgumentCaptor.forClass(ContaEntity.class);
                verify(repository, times(1)).save(captor.capture());

                ContaEntity updatedAccount = captor.getValue();

                assertEquals(BigDecimal.ZERO, updatedAccount.getSaldo());
            }
        }

        @Nested
        @DisplayName("Fail to update balance")
        class Fail {
            @Test
            @DisplayName("Should throw NullPointerException when account id is null")
            void shouldThrowNullPointerExceptionWhenAccountIdIsNull() {
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.decrementAmount(null, value));

                String expectedMessage = "Necessário informar o id da conta";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NullPointerException when value is null")
            void shouldThrowNullPointerExceptionWhenValueIsNull() {
                int accountId = new Random().nextInt();
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.decrementAmount(accountId, null));

                String expectedMessage = "Necessário informar o valor da transação";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("Should throw NotFoundException when bank account not found")
            void shouldThrowNotFoundExceptionWhenBankAccountNotFound() {
                Integer accountId = new Random().nextInt();
                BigDecimal value = BigDecimal.TEN;

                when(repository.findByConta(accountId)).thenReturn(Optional.empty());

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.decrementAmount(accountId, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
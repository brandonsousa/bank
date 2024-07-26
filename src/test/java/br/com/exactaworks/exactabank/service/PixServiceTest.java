package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.entity.ChavePixEntity;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import br.com.exactaworks.exactabank.exception.BadRequestException;
import br.com.exactaworks.exactabank.exception.NotFoundException;
import br.com.exactaworks.exactabank.repository.PixRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PixServiceTest {
    @Mock
    private PixRepository repository;
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private PixService pixService;

    @Nested
    class Store {
        @Nested
        @DisplayName("Store pix key successfully")
        class Successs {
            @Test
            @DisplayName("When everything is ok")
            void whenEverythingIsOk() {
                int bankAccount = 123456;
                TpChavePixEnum type = TpChavePixEnum.CPF;
                String key = "83923422083";
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(bankAccount)).thenReturn(entity);
                when(repository.existsByIdContaAndTpChave(entity.getId(), type)).thenReturn(false);
                when(repository.existsByChave(key)).thenReturn(false);

                pixService.store(bankAccount, type, key);

                ArgumentCaptor<ChavePixEntity> captor = ArgumentCaptor.forClass(ChavePixEntity.class);

                verify(repository, times(1)).save(captor.capture());

                ChavePixEntity captured = captor.getValue();

                assertNotNull(captured);
                assertNotNull(captured.getId());
                assertEquals(entity.getId(), captured.getIdConta());
                assertEquals(type, captured.getTpChave());
                assertEquals(key, captured.getChave());
            }
        }

        @Nested
        @DisplayName("Fail to store pix key")
        class Fail {
            @Test
            @DisplayName("When bank account is null")
            void whenBankAccountIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> pixService.store(null, TpChavePixEnum.CPF, "12345678901"));

                String expectedMessage = "Conta não pode ser nula";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When pix key type is null")
            void whenPixKeyTypeIsNull() {
                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> pixService.store(123456, null, "12345678901"));

                String expectedMessage = "Tipo de chave não pode ser nulo";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When bankAccountService throws NotFoundException")
            void whenBankAccountServiceThrowsNotFoundException() {
                int bankAccount = 123456;
                NotFoundException expectedException = new NotFoundException("Conta não encontrada");

                when(bankAccountService.findByConta(bankAccount)).thenThrow(expectedException);

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> pixService.store(bankAccount, TpChavePixEnum.CPF, "12345678901"));

                assertEquals(expectedException, exception);
            }

            @Test
            @DisplayName("When bankAccountService returns null should throw NotFoundException")
            void whenBankAccountServiceReturnsNullShouldThrowNotFoundException() {
                int bankAccount = 123456;

                when(bankAccountService.findByConta(bankAccount)).thenReturn(null);

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> pixService.store(bankAccount, TpChavePixEnum.CPF, "12345678901"));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When already exists pix key for account and type should throw BadRequestException")
            void whenAlreadyExistsPixKeyForAccountAndTypeShouldThrowBadRequestException() {
                int bankAccount = 123456;
                TpChavePixEnum type = TpChavePixEnum.CPF;
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(bankAccount)).thenReturn(entity);
                when(repository.existsByIdContaAndTpChave(entity.getId(), type)).thenReturn(true);

                BadRequestException exception = assertThrows(BadRequestException.class,
                        () -> pixService.store(bankAccount, type, "12345678901"));

                String expectedMessage = "Já existe chave pix para a conta";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When already exists pix key should throw BadRequestException")
            void whenAlreadyExistsPixKeyShouldThrowBadRequestException() {
                int bankAccount = 123456;
                TpChavePixEnum type = TpChavePixEnum.CPF;
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(bankAccount)).thenReturn(entity);
                when(repository.existsByIdContaAndTpChave(entity.getId(), type)).thenReturn(false);
                when(repository.existsByChave("12345678901")).thenReturn(true);

                BadRequestException exception = assertThrows(BadRequestException.class,
                        () -> pixService.store(bankAccount, type, "12345678901"));

                String expectedMessage = "Já existe chave pix";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
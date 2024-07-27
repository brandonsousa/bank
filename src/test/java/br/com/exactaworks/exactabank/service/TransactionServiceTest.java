package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import br.com.exactaworks.exactabank.enums.TpTransacaoEnum;
import br.com.exactaworks.exactabank.exception.NotFoundException;
import br.com.exactaworks.exactabank.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransactionService service;

    @Nested
    class Deposit {
        @Nested
        @DisplayName("Should create a deposit transaction successfully")
        class Success {
            @Test
            @DisplayName("When all parameters are valid")
            void whenAllParametersAreValid() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;
                ContaEntity entity = new ContaEntity();


                when(bankAccountService.findByConta(accountId)).thenReturn(entity);

                service.deposit(accountId, value);

                ArgumentCaptor<TransacaoEntity> captor = ArgumentCaptor.forClass(TransacaoEntity.class);
                verify(repository, times(1)).save(captor.capture());
                TransacaoEntity captured = captor.getValue();

                assertNotNull(captured.getId());
                assertEquals(TpTransacaoEnum.DEPOSITO, captured.getTpTransacao());
                assertNull(captured.getIdContaOrigem());
                assertNull(captured.getContaOrigem());
                assertNull(captured.getIdChavePixDestino());
                assertNull(captured.getChavePixDestino());
                assertEquals(entity.getId(), captured.getIdContaDestino());
                assertEquals(entity, captured.getContaDestino());
                assertEquals(value, captured.getValor());
            }
        }

        @Nested
        @DisplayName("Should fail to create a deposit transaction")
        class Fail {
            @Test
            @DisplayName("When account id is null should throw an NullPointerException")
            void whenAccountIdIsNullShouldThrowAnNullPointerException() {
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.deposit(null, value));

                String expectedMessage = "Necessário informar o id da conta";
                String actualMessage = exception.getMessage();

                assert (actualMessage.contains(expectedMessage));
            }

            @Test
            @DisplayName("When value is null should throw an NullPointerException")
            void whenValueIsNullShouldThrowAnNullPointerException() {
                Integer accountId = 1;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.deposit(accountId, null));

                String expectedMessage = "Necessário informar o valor do depósito";
                String actualMessage = exception.getMessage();

                assert (actualMessage.contains(expectedMessage));
            }

            @Test
            @DisplayName("When bankAccountService.findByConta throws an NotFoundException should throw a " +
                    "NotFoundException")
            void whenBankAccountServiceThrowsAnNotFoundExceptionShouldThrowANotFoundException() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;

                when(bankAccountService.findByConta(accountId)).thenThrow(
                        new NotFoundException("Conta não encontrada"));

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.deposit(accountId, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assert (actualMessage.contains(expectedMessage));
            }

            @Test
            @DisplayName("When bankAccountService.findByConta returns null should throw a NotFoundException")
            void whenBankAccountServiceFindByContaReturnsNullShouldThrowANotFoundException() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;

                when(bankAccountService.findByConta(accountId)).thenReturn(null);

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.deposit(accountId, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assert (actualMessage.contains(expectedMessage));
            }
        }
    }
}
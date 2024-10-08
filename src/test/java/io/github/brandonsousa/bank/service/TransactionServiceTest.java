package io.github.brandonsousa.bank.service;

import io.github.brandonsousa.bank.entity.ChavePixEntity;
import io.github.brandonsousa.bank.entity.ContaEntity;
import io.github.brandonsousa.bank.entity.TransacaoEntity;
import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import io.github.brandonsousa.bank.enums.TpTransacaoEnum;
import io.github.brandonsousa.bank.exception.BadRequestException;
import io.github.brandonsousa.bank.exception.NotFoundException;
import io.github.brandonsousa.bank.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository repository;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private PixService pixService;

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

    @Nested
    class Pix {
        @Nested
        @DisplayName("Should create a pix transaction successfully")
        class Success {
            @Test
            @DisplayName("When all parameters are valid")
            void whenAllParametersAreValid() {
                int accountId = new Random().nextInt();
                BigDecimal value = BigDecimal.TEN;
                TpChavePixEnum pixType = TpChavePixEnum.EMAIL;
                String pixKey = "email@email.com";
                ContaEntity account = ContaEntity.builder().conta(accountId).build();
                ChavePixEntity pix = ChavePixEntity.builder()
                        .conta(ContaEntity.builder().conta(new Random().nextInt()).build()).build();

                when(bankAccountService.findByConta(accountId)).thenReturn(account);
                when(pixService.findByTypeAndKeyWithAccount(pixType, pixKey)).thenReturn(pix);

                service.pix(accountId, pixType, pixKey, value);

                ArgumentCaptor<TransacaoEntity> transacaoEntityArgumentCaptor = ArgumentCaptor.forClass(
                        TransacaoEntity.class);
                verify(bankAccountService, times(1)).decrementAmount(accountId, value);
                verify(repository, times(1)).save(transacaoEntityArgumentCaptor.capture());
                verify(bankAccountService, times(1)).incrementAmount(pix.getConta().getConta(), value);

                TransacaoEntity captured = transacaoEntityArgumentCaptor.getValue();

                assertNotNull(captured.getId());
                assertEquals(TpTransacaoEnum.PIX, captured.getTpTransacao());
                assertEquals(account.getId(), captured.getIdContaOrigem());
                assertEquals(account, captured.getContaOrigem());
                assertEquals(pix.getId(), captured.getIdChavePixDestino());
                assertEquals(pix, captured.getChavePixDestino());
                assertEquals(pix.getConta().getId(), captured.getIdContaDestino());
                assertEquals(pix.getConta(), captured.getContaDestino());
            }
        }

        @Nested
        @DisplayName("Fail to create a pix transaction")
        class Fail {
            @Test
            @DisplayName("When pix and account are the same should throw a BadRequestException")
            void whenPixAndAccountAreTheSameShouldThrowABadRequestException() {
                int accountId = new Random().nextInt();
                BigDecimal value = BigDecimal.TEN;
                TpChavePixEnum pixType = TpChavePixEnum.EMAIL;
                String pixKey = "email@email.com";
                ContaEntity account = ContaEntity.builder().conta(accountId).build();
                ChavePixEntity pix = ChavePixEntity.builder().conta(account).build();

                when(bankAccountService.findByConta(accountId)).thenReturn(account);
                when(pixService.findByTypeAndKeyWithAccount(pixType, pixKey)).thenReturn(pix);

                BadRequestException exception = assertThrows(BadRequestException.class,
                        () -> service.pix(accountId, pixType, pixKey, value));

                String expectedMessage = "Não é possível realizar uma transação entre a mesma conta";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }

    @Nested
    class Withdraw {
        @Nested
        @DisplayName("Should create a withdraw transaction successfully")
        class Success {
            @Test
            @DisplayName("When all parameters are valid")
            void whenAllParametersAreValid() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(accountId)).thenReturn(entity);

                service.withdraw(accountId, value);

                ArgumentCaptor<TransacaoEntity> captor = ArgumentCaptor.forClass(TransacaoEntity.class);
                verify(repository, times(1)).save(captor.capture());
                verify(bankAccountService, times(1)).decrementAmount(accountId, value);

                TransacaoEntity captured = captor.getValue();

                assertNotNull(captured.getId());
                assertEquals(TpTransacaoEnum.SAQUE, captured.getTpTransacao());
                assertEquals(entity.getId(), captured.getIdContaOrigem());
                assertEquals(entity, captured.getContaOrigem());
                assertNull(captured.getIdChavePixDestino());
                assertNull(captured.getChavePixDestino());
                assertNull(captured.getIdContaDestino());
                assertNull(captured.getContaDestino());
                assertEquals(value.negate(), captured.getValor());
            }
        }
    }

    @Nested
    class findAllByConta {
        @Nested
        class Success {
            @Test
            @DisplayName("When all parameters are valid")
            void whenAllParametersAreValid() {
                Integer accountId = 1;
                Integer page = 0;
                Integer size = 10;
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(accountId)).thenReturn(entity);

                service.findAllByConta(accountId, page, size);

                ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
                verify(repository, times(1)).findAllByIdContaOrigemOrIdContaDestino(eq(entity.getId()),
                        eq(entity.getId()), captor.capture());

                Pageable captured = captor.getValue();

                assertEquals(page, captured.getPageNumber());
                assertEquals(size, captured.getPageSize());
            }
        }
    }

    @Nested
    class Recharge {
        @Nested
        @DisplayName("Should create a recharge transaction successfully")
        class Success {
            @Test
            @DisplayName("When all parameters are valid")
            void whenAllParametersAreValid() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;
                String phone = "11999999999";
                ContaEntity entity = new ContaEntity();

                when(bankAccountService.findByConta(accountId)).thenReturn(entity);

                service.recharge(accountId, phone, value);
            }
        }

        @Nested
        @DisplayName("Fail to create a recharge transaction")
        class Fail {
            @Test
            @DisplayName("When account id is null should throw an NullPointerException")
            void whenAccountIdIsNullShouldThrowAnNullPointerException() {
                BigDecimal value = BigDecimal.TEN;
                String phone = "123456789";

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.recharge(null, phone, value));

                String expectedMessage = "Necessário informar o id da conta";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When phone is null should throw an NullPointerException")
            void whenPhoneIsNullShouldThrowAnNullPointerException() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.recharge(accountId, null, value));

                String expectedMessage = "Necessário informar o número do telefone";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When value is null should throw an NullPointerException")
            void whenValueIsNullShouldThrowAnNullPointerException() {
                Integer accountId = 1;
                String phone = "123456789";

                NullPointerException exception = assertThrows(NullPointerException.class,
                        () -> service.recharge(accountId, phone, null));

                String expectedMessage = "Necessário informar o valor da recarga";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When bankAccountService.findByConta throws an NotFoundException should throw a " +
                    "NotFoundException")
            void whenBankAccountServiceThrowsAnNotFoundExceptionShouldThrowANotFoundException() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;
                String phone = "123456789";

                when(bankAccountService.findByConta(accountId)).thenThrow(
                        new NotFoundException("Conta não encontrada"));

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.recharge(accountId, phone, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }

            @Test
            @DisplayName("When bankAccountService.findByConta returns null should throw a NotFoundException")
            void whenBankAccountServiceFindByContaReturnsNullShouldThrowANotFoundException() {
                Integer accountId = 1;
                BigDecimal value = BigDecimal.TEN;
                String phone = "123456789";

                when(bankAccountService.findByConta(accountId)).thenReturn(null);

                NotFoundException exception = assertThrows(NotFoundException.class,
                        () -> service.recharge(accountId, phone, value));

                String expectedMessage = "Conta não encontrada";
                String actualMessage = exception.getMessage();

                assertEquals(expectedMessage, actualMessage);
            }
        }
    }
}
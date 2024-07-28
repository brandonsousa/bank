package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.entity.ChavePixEntity;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import br.com.exactaworks.exactabank.exception.BadRequestException;
import br.com.exactaworks.exactabank.exception.NotFoundException;
import br.com.exactaworks.exactabank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository repository;
    private final BankAccountService bankAccountService;
    private final PixService pixService;

    @Transactional(propagation = Propagation.REQUIRED)
    public TransacaoEntity deposit(Integer accountId, BigDecimal value) {
        Objects.requireNonNull(accountId, "Necessário informar o id da conta");
        Objects.requireNonNull(value, "Necessário informar o valor do depósito");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));

        TransacaoEntity transaction = TransacaoEntity.deposit(account, value);
        TransacaoEntity savedTransaction = repository.save(transaction);

        bankAccountService.incrementAmount(accountId, value);

        return savedTransaction;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TransacaoEntity pix(Integer accountId, TpChavePixEnum pixType, String pixKey, BigDecimal value) {
        Objects.requireNonNull(accountId, "Necessário informar o id da conta");
        Objects.requireNonNull(pixType, "Necessário informar o tipo da chave pix");
        Objects.requireNonNull(pixKey, "Necessário informar a chave pix");
        Objects.requireNonNull(value, "Necessário informar o valor da transação");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));
        ChavePixEntity pix = Optional.ofNullable(pixService.findByTypeAndKeyWithAccount(pixType, pixKey))
                .orElseThrow(() -> new NotFoundException("Chave pix não encontrada"));

        if (Objects.equals(account, pix.getConta())) {
            log.error("Não é possível realizar uma transação entre a mesma conta");
            throw new BadRequestException("Não é possível realizar uma transação entre a mesma conta");
        }

        bankAccountService.decrementAmount(accountId, value);

        TransacaoEntity transaction = TransacaoEntity.pix(account, pix, value);
        TransacaoEntity savedTransaction = repository.save(transaction);

        bankAccountService.incrementAmount(pix.getConta().getConta(), value);

        return savedTransaction;
    }
}
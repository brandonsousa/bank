package io.github.brandonsousa.bank.service;

import io.github.brandonsousa.bank.entity.ChavePixEntity;
import io.github.brandonsousa.bank.entity.ContaEntity;
import io.github.brandonsousa.bank.entity.TransacaoEntity;
import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import io.github.brandonsousa.bank.exception.BadRequestException;
import io.github.brandonsousa.bank.exception.NotFoundException;
import io.github.brandonsousa.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private static final String ACCOUNT_ID_MUST_BE_INFORMED = "Necessário informar o id da conta";
    private static final String ACCOUNT_NOT_FOUND = "Conta não encontrada";

    @Transactional(propagation = Propagation.REQUIRED)
    public TransacaoEntity deposit(Integer accountId, BigDecimal value) {
        Objects.requireNonNull(accountId, ACCOUNT_ID_MUST_BE_INFORMED);
        Objects.requireNonNull(value, "Necessário informar o valor do depósito");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        TransacaoEntity transaction = TransacaoEntity.deposit(account, value);
        TransacaoEntity savedTransaction = repository.save(transaction);

        bankAccountService.incrementAmount(accountId, value);

        return savedTransaction;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public TransacaoEntity pix(Integer accountId, TpChavePixEnum pixType, String pixKey, BigDecimal value) {
        Objects.requireNonNull(accountId, ACCOUNT_ID_MUST_BE_INFORMED);
        Objects.requireNonNull(pixType, "Necessário informar o tipo da chave pix");
        Objects.requireNonNull(pixKey, "Necessário informar a chave pix");
        Objects.requireNonNull(value, "Necessário informar o valor da transação");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));
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

    @Transactional(propagation = Propagation.REQUIRED)
    public TransacaoEntity withdraw(Integer accountId, BigDecimal value) {
        Objects.requireNonNull(accountId, ACCOUNT_ID_MUST_BE_INFORMED);
        Objects.requireNonNull(value, "Necessário informar o valor do saque");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        bankAccountService.decrementAmount(accountId, value);

        TransacaoEntity transaction = TransacaoEntity.withdraw(account, value);
        return repository.save(transaction);
    }

    public Page<TransacaoEntity> findAllByConta(Integer accountId, Integer page, Integer size) {
        Objects.requireNonNull(accountId, ACCOUNT_ID_MUST_BE_INFORMED);
        Objects.requireNonNull(page, "Necessário informar o número da página");
        Objects.requireNonNull(size, "Necessário informar o tamanho da página");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        return repository.findAllByIdContaOrigemOrIdContaDestino(account.getId(), account.getId(), pageable);
    }

    public TransacaoEntity recharge(Integer accountId, String phone, BigDecimal value) {
        Objects.requireNonNull(accountId, ACCOUNT_ID_MUST_BE_INFORMED);
        Objects.requireNonNull(phone, "Necessário informar o número do telefone");
        Objects.requireNonNull(value, "Necessário informar o valor da recarga");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException(ACCOUNT_NOT_FOUND));

        bankAccountService.decrementAmount(accountId, value);
        TransacaoEntity transaction = TransacaoEntity.recharge(account, value, phone);

        return repository.save(transaction);
    }
}
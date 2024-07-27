package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import br.com.exactaworks.exactabank.exception.NotFoundException;
import br.com.exactaworks.exactabank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository repository;
    private final BankAccountService bankAccountService;

    public TransacaoEntity deposit(Integer accountId, BigDecimal value) {
        Objects.requireNonNull(accountId, "Necessário informar o id da conta");
        Objects.requireNonNull(value, "Necessário informar o valor do depósito");

        ContaEntity account = Optional.ofNullable(bankAccountService.findByConta(accountId))
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));

        TransacaoEntity transaction = TransacaoEntity.deposit(account, value);
        TransacaoEntity savedTransaction = repository.save(transaction);

        bankAccountService.updateBalance(accountId, value);

        return savedTransaction;
    }
}
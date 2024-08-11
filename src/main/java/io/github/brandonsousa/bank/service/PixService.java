package io.github.brandonsousa.bank.service;

import io.github.brandonsousa.bank.entity.ChavePixEntity;
import io.github.brandonsousa.bank.entity.ContaEntity;
import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import io.github.brandonsousa.bank.exception.BadRequestException;
import io.github.brandonsousa.bank.exception.NotFoundException;
import io.github.brandonsousa.bank.repository.PixRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixService {
    private final PixRepository repository;
    private final BankAccountService bankAccountService;

    public ChavePixEntity store(Integer bankAccount, TpChavePixEnum type, String key) {
        Objects.requireNonNull(bankAccount, "Conta não pode ser nula");
        Objects.requireNonNull(type, "Tipo de chave não pode ser nulo");

        log.debug("Creating pix key for account {}", bankAccount);

        ContaEntity bankAccountEntity = Optional.ofNullable(bankAccountService.findByConta(bankAccount))
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));

        if (repository.existsByIdContaAndTpChave(bankAccountEntity.getId(), type)) {
            log.error("Pix key already exists for account {}", bankAccount);
            throw new BadRequestException("Já existe chave pix para a conta");
        }

        if (repository.existsByChave(key)) {
            log.error("Pix key already exists for key {}", key);
            throw new BadRequestException("Já existe chave pix");
        }

        ChavePixEntity pixKey = ChavePixEntity.create(bankAccountEntity, type, key);

        return repository.save(pixKey);
    }

    public ChavePixEntity findByTypeAndKeyWithAccount(TpChavePixEnum type, String key) {
        Objects.requireNonNull(type, "Tipo de chave não pode ser nulo");
        Objects.requireNonNull(key, "Chave não pode ser nula");

        log.debug("Finding pix key by type {} and key {}", type, key);

        return repository.findByTpChaveAndChave(type, key)
                .orElseThrow(() -> new NotFoundException("Chave pix não encontrada"));
    }
}

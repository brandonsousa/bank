package br.com.exactaworks.exactabank.service;

import br.com.exactaworks.exactabank.dvo.DocumentoDVO;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import br.com.exactaworks.exactabank.exception.BadRequestException;
import br.com.exactaworks.exactabank.exception.NotFoundException;
import br.com.exactaworks.exactabank.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankAccountService {
    private final BankAccountRepository repository;

    @Transactional(propagation = Propagation.REQUIRED)
    public ContaEntity store(String name, String document) {
        Objects.requireNonNull(name, "Nome não pode ser nulo");
        Objects.requireNonNull(document, "Documento não pode ser nulo");

        log.debug("Creating bank account for {}", name);

        DocumentoDVO documentDVO = new DocumentoDVO(document);

        if (repository.existsByDocumento(documentDVO.value())) {
            log.error("Bank account already exists for {}", documentDVO.value());
            throw new BadRequestException("Já existe conta para o cliente");
        }

        Integer account = repository.getNextContaValue();
        Objects.requireNonNull(account, "Conta não pode ser nula");

        ContaEntity bankAccount = ContaEntity.create(name, documentDVO, account);

        return repository.save(bankAccount);
    }

    public ContaEntity findByConta(Integer account) {
        Objects.requireNonNull(account, "Conta não pode ser nula");

        log.debug("Finding bank account by account {}", account);

        return repository.findByConta(account)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada"));
    }

    public Page<ContaEntity> findAll(int page, int size, String filter) {
        Objects.requireNonNull(page, "Página não pode ser nula");
        Objects.requireNonNull(size, "Tamanho não pode ser nulo");

        log.debug("Finding all bank accounts");
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByFilter(filter, pageable);
    }
}

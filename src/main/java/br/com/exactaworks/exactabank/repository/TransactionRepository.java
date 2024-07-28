package br.com.exactaworks.exactabank.repository;

import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransacaoEntity, UUID> {
    @EntityGraph(attributePaths = {"contaOrigem", "chavePixDestino", "contaDestino"})
    Page<TransacaoEntity> findAllByIdContaOrigemOrIdContaDestino(UUID idContaOrigem, UUID idContaDestino,
            Pageable pageable);
}

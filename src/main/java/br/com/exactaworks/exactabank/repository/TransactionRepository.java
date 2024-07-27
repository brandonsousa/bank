package br.com.exactaworks.exactabank.repository;

import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransacaoEntity, UUID> {
}

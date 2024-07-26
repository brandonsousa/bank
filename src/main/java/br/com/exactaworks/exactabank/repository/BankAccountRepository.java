package br.com.exactaworks.exactabank.repository;


import br.com.exactaworks.exactabank.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<ContaEntity, UUID> {
    boolean existsByDocumento(String document);

    @Query(value = "SELECT nextval('seq_conta_conta')", nativeQuery = true)
    Integer getNextContaValue();

    Optional<ContaEntity> findByConta(Integer account);
}

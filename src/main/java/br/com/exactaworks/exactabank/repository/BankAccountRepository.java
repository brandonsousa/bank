package br.com.exactaworks.exactabank.repository;


import br.com.exactaworks.exactabank.entity.ContaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<ContaEntity, UUID> {
    boolean existsByDocumento(String document);

    @Query(value = "SELECT nextval('seq_conta_conta')", nativeQuery = true)
    Integer getNextContaValue();

    Optional<ContaEntity> findByConta(Integer account);

    @Query("""
            SELECT c FROM ContaEntity c
            WHERE (:filter IS NULL OR :filter = '' 
                OR c.nome LIKE CONCAT(:filter, '%') 
                OR c.documento LIKE CONCAT(:filter, '%') 
                OR cast(c.conta as string) LIKE CONCAT(:filter, '%'))
            """)
    Page<ContaEntity> findByFilter(String filter, Pageable pageable);
}

package io.github.brandonsousa.bank.repository;

import io.github.brandonsousa.bank.entity.ChavePixEntity;
import io.github.brandonsousa.bank.enums.TpChavePixEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PixRepository extends JpaRepository<ChavePixEntity, UUID> {
    boolean existsByIdContaAndTpChave(UUID idConta, TpChavePixEnum tpChave);

    boolean existsByChave(String chave);

    @EntityGraph(attributePaths = {"conta"})
    Optional<ChavePixEntity> findByTpChaveAndChave(TpChavePixEnum tpChave, String chave);
}

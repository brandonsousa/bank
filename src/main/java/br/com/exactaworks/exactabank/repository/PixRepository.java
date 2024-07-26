package br.com.exactaworks.exactabank.repository;

import br.com.exactaworks.exactabank.entity.ChavePixEntity;
import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PixRepository extends JpaRepository<ChavePixEntity, UUID> {
    boolean existsByIdContaAndTpChave(UUID idConta, TpChavePixEnum tpChave);

    boolean existsByChave(String chave);
}

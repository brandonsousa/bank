package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.entity.abstracts.AbstractEntity;
import br.com.exactaworks.exactabank.enums.TpChavePixEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "chave_pix", indexes = {
        @Index(name = "PK_ID_CHAVE_PIX", columnList = "id", unique = true),
        @Index(name = "IX_CHAVE_PIX_CHAVE", columnList = "chave", unique = true),
        @Index(name = "IX_CHAVE_PIX_ID_CONTA", columnList = "id_conta"),
        @Index(name = "IX_CHAVE_PIX_ID_CONTA_TP_CHAVE", columnList = "id_conta, tp_chave", unique = true)
})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class ChavePixEntity extends AbstractEntity {
    @Column(name = "id_conta", nullable = false)
    private UUID idConta;

    @Column(name = "tp_chave", nullable = false, length = 9)
    @Enumerated(EnumType.STRING)
    private TpChavePixEnum tpChave;

    @Column(name = "chave", nullable = false, length = 254)
    private String chave;

    @OneToMany(mappedBy = "idChavePixDestino")
    private Set<TransacaoEntity> transacaos = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_conta", nullable = false, insertable = false, updatable = false)
    private ContaEntity conta;
}
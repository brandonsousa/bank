package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.entity.abstracts.AbstractEntity;
import br.com.exactaworks.exactabank.enums.TpTransacaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transacao", indexes = {
        @Index(name = "pk_id_transacao", columnList = "id", unique = true),
        @Index(name = "idx_id_conta_origem", columnList = "id_conta_origem"),
        @Index(name = "idx_id_chave_pix_destino", columnList = "id_chave_pix_destino"),
        @Index(name = "idx_id_conta_destino", columnList = "id_conta_destino")
})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class TransacaoEntity extends AbstractEntity {
    @Column(name = "id_conta_origem")
    private UUID idContaOrigem;

    @Column(name = "id_chave_pix_destino")
    private UUID idChavePixDestino;

    @Column(name = "id_conta_destino")
    private UUID idContaDestino;

    @Column(name = "tp_transacao", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private TpTransacaoEnum tpTransacao;

    @Column(name = "vl_transacao", nullable = false, precision = 10, scale = 2)
    private BigDecimal vlTransacao;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conta_origem", insertable = false, updatable = false)
    private ContaEntity contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chave_pix_destino", insertable = false, updatable = false)
    private ChavePixEntity chavePixDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_conta_destino", insertable = false, updatable = false)
    private ContaEntity contaDestino;
}
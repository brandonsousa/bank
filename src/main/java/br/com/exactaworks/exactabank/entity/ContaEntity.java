package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.entity.abstracts.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta", indexes = {
        @Index(name = "PK_ID_CONTA", columnList = "id", unique = true),
        @Index(name = "IX_CONTA_DOCUMENTO", columnList = "documento", unique = true),
        @Index(name = "IX_CONTA_CONTA", columnList = "conta", unique = true)
})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
public class ContaEntity extends AbstractEntity {
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 11, unique = true)
    private String documento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conta_conta_seq")
    @SequenceGenerator(name = "conta_conta_seq", sequenceName = "seq_conta_conta", allocationSize = 1)
    private Integer conta;

    @Column(name = "dt_atualizacao", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dtAtualizacao;
}
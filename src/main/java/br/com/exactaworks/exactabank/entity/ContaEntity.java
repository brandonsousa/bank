package br.com.exactaworks.exactabank.entity;

import br.com.exactaworks.exactabank.dvo.DocumentoDVO;
import br.com.exactaworks.exactabank.entity.abstracts.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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

    @Column(nullable = false, unique = true)
    private Integer conta;

    @Column(name = "dt_atualizacao", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dtAtualizacao;

    public static ContaEntity create(String name, DocumentoDVO document, Integer accountNumber) {
        Objects.requireNonNull(name, "Nome não pode ser nulo");
        Objects.requireNonNull(document, "Documento não pode ser nulo");
        Objects.requireNonNull(accountNumber, "Conta não pode ser nulo");

        if (name.isBlank()) throw new IllegalArgumentException("Nome não pode ser vazio");

        return ContaEntity.builder()
                .id(UUID.randomUUID())
                .nome(name)
                .documento(document.value())
                .saldo(BigDecimal.ZERO)
                .conta(accountNumber)
                .build();
    }
}
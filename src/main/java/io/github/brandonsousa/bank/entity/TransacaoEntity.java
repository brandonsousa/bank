package io.github.brandonsousa.bank.entity;

import io.github.brandonsousa.bank.entity.abstracts.AbstractEntity;
import io.github.brandonsousa.bank.enums.TpTransacaoEnum;
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
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

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

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

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

    public static TransacaoEntity create(TpTransacaoEnum type, BigDecimal value, String description,
            ContaEntity origin, ChavePixEntity pixDestiny, ContaEntity accountDestiny) {
        Objects.requireNonNull(type, "Tipo de transação não pode ser nulo");
        Objects.requireNonNull(value, "Valor da transação não pode ser nulo");

        if (value.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Valor da transação deve ser maior que zero");

        UUID accountDestinyId = null;
        if (!type.equals(TpTransacaoEnum.DEPOSITO)) value = value.negate();
        else {
            Objects.requireNonNull(accountDestiny, "Conta destino não pode ser nula");
            value = value.abs();
            accountDestinyId = accountDestiny.getId();
        }

        UUID pixDestinyId = null;
        if (type.equals(TpTransacaoEnum.PIX)) {
            Objects.requireNonNull(origin, "Conta de origem não pode ser nula");
            Objects.requireNonNull(pixDestiny, "Chave PIX destino não pode ser nula");

            pixDestinyId = pixDestiny.getId();
            accountDestinyId = pixDestiny.getConta().getId();
        }

        if (type.equals(TpTransacaoEnum.SAQUE)) Objects.requireNonNull(origin, "Conta origem não pode ser nula");

        if (type.equals(TpTransacaoEnum.RECARGA)) {
            Objects.requireNonNull(origin, "Conta origem não pode ser nula");
            Objects.requireNonNull(description, "Número de telefone não pode ser nulo");

            final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");
            String cleaned = description.replaceAll("[^0-9]", "");
            if (!PHONE_PATTERN.matcher(cleaned).matches() && !Objects.equals(cleaned.length(), 11))
                throw new IllegalArgumentException("Número de telefone inválido");

        }

        return TransacaoEntity.builder()
                .id(UUID.randomUUID())
                .tpTransacao(type)
                .valor(value)
                .descricao(description)
                .idContaOrigem(Objects.isNull(origin) ? null : origin.getId())
                .idChavePixDestino(pixDestinyId)
                .idContaDestino(accountDestinyId)
                .contaOrigem(origin)
                .chavePixDestino(pixDestiny)
                .contaDestino(Objects.isNull(pixDestiny) ? accountDestiny : pixDestiny.getConta())
                .build();
    }

    public static TransacaoEntity deposit(ContaEntity account, BigDecimal value) {
        return create(TpTransacaoEnum.DEPOSITO, value, null, null, null, account);
    }

    public static TransacaoEntity pix(ContaEntity origin, ChavePixEntity destiny, BigDecimal value) {
        return create(TpTransacaoEnum.PIX, value, null, origin, destiny, null);
    }

    public static TransacaoEntity withdraw(ContaEntity account, BigDecimal value) {
        return create(TpTransacaoEnum.SAQUE, value, null, account, null, null);
    }

    public static TransacaoEntity recharge(ContaEntity account, BigDecimal value, String phone) {
        return create(TpTransacaoEnum.RECARGA, value, phone, account, null, null);
    }
}
package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.transaction.DepositStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.PixStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.RechargeStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.TransactionResponse;
import br.com.exactaworks.exactabank.api.response.transaction.WithdrawStoreResponse;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    @Mapping(source = "entity.contaDestino.conta", target = "conta")
    @Mapping(source = "entity.contaDestino.saldo", target = "saldo")
    DepositStoreResponse fromTransacaoEntity(TransacaoEntity entity);

    @Mapping(source = "entity.contaOrigem.conta", target = "contaOrigem")
    @Mapping(source = "entity.contaOrigem.saldo", target = "saldoContaOrigem")
    @Mapping(source = "entity.chavePixDestino.chave", target = "chavePixDestino")
    @Mapping(source = "entity.contaDestino.conta", target = "contaDestino")
    PixStoreResponse fromTransacaoEntityPix(TransacaoEntity entity);

    @Mapping(source = "entity.contaOrigem.conta", target = "contaOrigem")
    @Mapping(source = "entity.contaOrigem.saldo", target = "saldoContaOrigem")
    WithdrawStoreResponse fromTransacaoEntityWithdraw(TransacaoEntity entity);

    @Mapping(source = "entity.contaOrigem.conta", target = "contaOrigem")
    @Mapping(source = "entity.contaOrigem.nome", target = "usuarioOrigem")
    @Mapping(source = "entity.chavePixDestino.chave", target = "chavePixDestino")
    @Mapping(source = "entity.contaDestino.nome", target = "usuarioDestino")
    @Mapping(source = "entity.contaDestino.conta", target = "contaDestino")
    TransactionResponse fromTransacaoEntityToList(TransacaoEntity entity);

    default PagedModel<TransactionResponse> fromTransacaoPage(Page<TransacaoEntity> entityPage, int account) {
        Objects.requireNonNull(entityPage, "Página de transações não pode ser nula");

        List<TransactionResponse> dtoList = entityPage.getContent().stream().map(this::fromTransacaoEntityToList)
                .toList();

        dtoList.forEach(dto -> dto.adjustValueToOwner(account));

        return new PagedModel<>(new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements()));
    }


    @Mapping(source = "entity.contaOrigem.conta", target = "contaOrigem")
    @Mapping(source = "entity.descricao", target = "numeroTelefone")
    @Mapping(source = "entity.valor", target = "valor")
    RechargeStoreResponse fromTransacaoEntityRecharge(TransacaoEntity entity);
}
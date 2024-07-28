package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.transaction.DepositStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.PixStoreResponse;
import br.com.exactaworks.exactabank.api.response.transaction.WithdrawStoreResponse;
import br.com.exactaworks.exactabank.entity.TransacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

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
}
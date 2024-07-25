package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountStoreResponse;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountStoreResponse fromContaEntity(ContaEntity entity);
}

package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountListResponse;
import br.com.exactaworks.exactabank.api.response.banckAccount.BankAccountStoreResponse;
import br.com.exactaworks.exactabank.entity.ContaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountMapper INSTANCE = Mappers.getMapper(BankAccountMapper.class);

    BankAccountStoreResponse fromContaEntity(ContaEntity entity);

    List<BankAccountListResponse> fromContaEntities(List<ContaEntity> entities);

    default PagedModel<BankAccountListResponse> fromContaPage(Page<ContaEntity> entityPage) {
        Objects.requireNonNull(entityPage, "Página de contas não pode ser nula");

        List<BankAccountListResponse> dtoList = fromContaEntities(entityPage.getContent());
        return new PagedModel<>(new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements()));
    }
}

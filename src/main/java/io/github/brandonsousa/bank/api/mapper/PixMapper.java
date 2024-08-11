package io.github.brandonsousa.bank.api.mapper;

import io.github.brandonsousa.bank.api.response.pix.PixStoreResponse;
import io.github.brandonsousa.bank.entity.ChavePixEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PixMapper {
    PixMapper INSTANCE = Mappers.getMapper(PixMapper.class);

    @Mapping(source = "entity.conta.conta", target = "conta")
    PixStoreResponse fromChavePixEntity(ChavePixEntity entity);
}

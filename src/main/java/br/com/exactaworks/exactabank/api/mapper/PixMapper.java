package br.com.exactaworks.exactabank.api.mapper;

import br.com.exactaworks.exactabank.api.response.pix.PixStoreResponse;
import br.com.exactaworks.exactabank.entity.ChavePixEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PixMapper {
    PixMapper INSTANCE = Mappers.getMapper(PixMapper.class);

    @Mapping(source = "entity.conta.conta", target = "conta")
    PixStoreResponse fromChavePixEntity(ChavePixEntity entity);
}

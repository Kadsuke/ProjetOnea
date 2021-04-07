package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.TypeCommuneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TypeCommune} and its DTO {@link TypeCommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TypeCommuneMapper extends EntityMapper<TypeCommuneDTO, TypeCommune> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TypeCommuneDTO toDtoId(TypeCommune typeCommune);
}

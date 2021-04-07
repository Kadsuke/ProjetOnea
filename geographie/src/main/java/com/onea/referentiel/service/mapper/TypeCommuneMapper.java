package com.onea.referentiel.service.mapper;

import com.onea.referentiel.domain.*;
import com.onea.referentiel.service.dto.TypeCommuneDTO;
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

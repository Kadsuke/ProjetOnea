package com.onea.referentiel.service.mapper;

import com.onea.referentiel.domain.*;
import com.onea.referentiel.service.dto.CommuneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Commune} and its DTO {@link CommuneDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProvinceMapper.class, TypeCommuneMapper.class })
public interface CommuneMapper extends EntityMapper<CommuneDTO, Commune> {
    @Mapping(target = "province", source = "province", qualifiedByName = "id")
    @Mapping(target = "typeCommune", source = "typeCommune", qualifiedByName = "id")
    CommuneDTO toDto(Commune s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommuneDTO toDtoId(Commune commune);
}

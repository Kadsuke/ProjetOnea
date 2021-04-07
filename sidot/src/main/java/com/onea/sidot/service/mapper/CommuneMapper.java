package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.CommuneDTO;
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

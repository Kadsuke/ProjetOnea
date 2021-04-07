package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.LocaliteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Localite} and its DTO {@link LocaliteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommuneMapper.class })
public interface LocaliteMapper extends EntityMapper<LocaliteDTO, Localite> {
    @Mapping(target = "commune", source = "commune", qualifiedByName = "id")
    LocaliteDTO toDto(Localite s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocaliteDTO toDtoId(Localite localite);
}

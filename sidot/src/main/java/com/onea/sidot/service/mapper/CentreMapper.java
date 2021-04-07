package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.CentreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Centre} and its DTO {@link CentreDTO}.
 */
@Mapper(componentModel = "spring", uses = { CentreRegroupementMapper.class })
public interface CentreMapper extends EntityMapper<CentreDTO, Centre> {
    @Mapping(target = "centreRegroupement", source = "centreRegroupement", qualifiedByName = "id")
    CentreDTO toDto(Centre s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentreDTO toDtoId(Centre centre);
}

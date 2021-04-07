package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.CentreRegroupementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CentreRegroupement} and its DTO {@link CentreRegroupementDTO}.
 */
@Mapper(componentModel = "spring", uses = { DirectionRegionaleMapper.class })
public interface CentreRegroupementMapper extends EntityMapper<CentreRegroupementDTO, CentreRegroupement> {
    @Mapping(target = "directionRegionale", source = "directionRegionale", qualifiedByName = "id")
    CentreRegroupementDTO toDto(CentreRegroupement s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CentreRegroupementDTO toDtoId(CentreRegroupement centreRegroupement);
}

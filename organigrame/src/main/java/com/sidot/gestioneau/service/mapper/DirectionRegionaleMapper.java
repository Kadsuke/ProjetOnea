package com.sidot.gestioneau.service.mapper;

import com.sidot.gestioneau.domain.*;
import com.sidot.gestioneau.service.dto.DirectionRegionaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DirectionRegionale} and its DTO {@link DirectionRegionaleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DirectionRegionaleMapper extends EntityMapper<DirectionRegionaleDTO, DirectionRegionale> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DirectionRegionaleDTO toDtoId(DirectionRegionale directionRegionale);
}

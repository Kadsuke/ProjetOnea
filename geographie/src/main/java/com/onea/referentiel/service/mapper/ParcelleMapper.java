package com.onea.referentiel.service.mapper;

import com.onea.referentiel.domain.*;
import com.onea.referentiel.service.dto.ParcelleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parcelle} and its DTO {@link ParcelleDTO}.
 */
@Mapper(componentModel = "spring", uses = { LotMapper.class })
public interface ParcelleMapper extends EntityMapper<ParcelleDTO, Parcelle> {
    @Mapping(target = "lot", source = "lot", qualifiedByName = "id")
    ParcelleDTO toDto(Parcelle s);
}

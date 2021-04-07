package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.ParcelleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Parcelle} and its DTO {@link ParcelleDTO}.
 */
@Mapper(componentModel = "spring", uses = { LotMapper.class })
public interface ParcelleMapper extends EntityMapper<ParcelleDTO, Parcelle> {
    @Mapping(target = "lot", source = "lot", qualifiedByName = "id")
    ParcelleDTO toDto(Parcelle s);
}

package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.LotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lot} and its DTO {@link LotDTO}.
 */
@Mapper(componentModel = "spring", uses = { SectionMapper.class })
public interface LotMapper extends EntityMapper<LotDTO, Lot> {
    @Mapping(target = "section", source = "section", qualifiedByName = "id")
    LotDTO toDto(Lot s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LotDTO toDtoId(Lot lot);
}

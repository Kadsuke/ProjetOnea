package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.RegionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Region} and its DTO {@link RegionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RegionMapper extends EntityMapper<RegionDTO, Region> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RegionDTO toDtoId(Region region);
}

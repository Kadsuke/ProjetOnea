package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.SiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Site} and its DTO {@link SiteDTO}.
 */
@Mapper(componentModel = "spring", uses = { CentreMapper.class })
public interface SiteMapper extends EntityMapper<SiteDTO, Site> {
    @Mapping(target = "centre", source = "centre", qualifiedByName = "id")
    SiteDTO toDto(Site s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteDTO toDtoId(Site site);
}

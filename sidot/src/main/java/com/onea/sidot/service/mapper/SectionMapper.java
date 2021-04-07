package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.SectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Section} and its DTO {@link SectionDTO}.
 */
@Mapper(componentModel = "spring", uses = { SecteurMapper.class })
public interface SectionMapper extends EntityMapper<SectionDTO, Section> {
    @Mapping(target = "secteur", source = "secteur", qualifiedByName = "id")
    SectionDTO toDto(Section s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SectionDTO toDtoId(Section section);
}

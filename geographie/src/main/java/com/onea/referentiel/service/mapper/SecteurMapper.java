package com.onea.referentiel.service.mapper;

import com.onea.referentiel.domain.*;
import com.onea.referentiel.service.dto.SecteurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Secteur} and its DTO {@link SecteurDTO}.
 */
@Mapper(componentModel = "spring", uses = { LocaliteMapper.class })
public interface SecteurMapper extends EntityMapper<SecteurDTO, Secteur> {
    @Mapping(target = "localite", source = "localite", qualifiedByName = "id")
    SecteurDTO toDto(Secteur s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SecteurDTO toDtoId(Secteur secteur);
}

package com.sidot.gestioneau.service.mapper;

import com.sidot.gestioneau.domain.*;
import com.sidot.gestioneau.service.dto.AgentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring", uses = { SiteMapper.class })
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "site", source = "site", qualifiedByName = "id")
    @Mapping(target = "site", source = "site", qualifiedByName = "id")
    AgentDTO toDto(Agent s);
}

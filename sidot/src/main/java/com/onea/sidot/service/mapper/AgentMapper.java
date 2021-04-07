package com.onea.sidot.service.mapper;

import com.onea.sidot.domain.*;
import com.onea.sidot.service.dto.AgentDTO;
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

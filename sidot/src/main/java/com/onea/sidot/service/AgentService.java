package com.onea.sidot.service;

import com.onea.sidot.domain.Agent;
import com.onea.sidot.repository.AgentRepository;
import com.onea.sidot.service.dto.AgentDTO;
import com.onea.sidot.service.mapper.AgentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Agent}.
 */
@Service
@Transactional
public class AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final AgentRepository agentRepository;

    private final AgentMapper agentMapper;

    public AgentService(AgentRepository agentRepository, AgentMapper agentMapper) {
        this.agentRepository = agentRepository;
        this.agentMapper = agentMapper;
    }

    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<AgentDTO> save(AgentDTO agentDTO) {
        log.debug("Request to save Agent : {}", agentDTO);
        return agentRepository.save(agentMapper.toEntity(agentDTO)).map(agentMapper::toDto);
    }

    /**
     * Partially update a agent.
     *
     * @param agentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<AgentDTO> partialUpdate(AgentDTO agentDTO) {
        log.debug("Request to partially update Agent : {}", agentDTO);

        return agentRepository
            .findById(agentDTO.getId())
            .map(
                existingAgent -> {
                    agentMapper.partialUpdate(existingAgent, agentDTO);
                    return existingAgent;
                }
            )
            .flatMap(agentRepository::save)
            .map(agentMapper::toDto);
    }

    /**
     * Get all the agents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<AgentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agents");
        return agentRepository.findAllBy(pageable).map(agentMapper::toDto);
    }

    /**
     * Returns the number of agents available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return agentRepository.count();
    }

    /**
     * Get one agent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<AgentDTO> findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findById(id).map(agentMapper::toDto);
    }

    /**
     * Delete the agent by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        return agentRepository.deleteById(id);
    }
}

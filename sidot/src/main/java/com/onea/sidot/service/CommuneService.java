package com.onea.sidot.service;

import com.onea.sidot.domain.Commune;
import com.onea.sidot.repository.CommuneRepository;
import com.onea.sidot.service.dto.CommuneDTO;
import com.onea.sidot.service.mapper.CommuneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Commune}.
 */
@Service
@Transactional
public class CommuneService {

    private final Logger log = LoggerFactory.getLogger(CommuneService.class);

    private final CommuneRepository communeRepository;

    private final CommuneMapper communeMapper;

    public CommuneService(CommuneRepository communeRepository, CommuneMapper communeMapper) {
        this.communeRepository = communeRepository;
        this.communeMapper = communeMapper;
    }

    /**
     * Save a commune.
     *
     * @param communeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CommuneDTO> save(CommuneDTO communeDTO) {
        log.debug("Request to save Commune : {}", communeDTO);
        return communeRepository.save(communeMapper.toEntity(communeDTO)).map(communeMapper::toDto);
    }

    /**
     * Partially update a commune.
     *
     * @param communeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CommuneDTO> partialUpdate(CommuneDTO communeDTO) {
        log.debug("Request to partially update Commune : {}", communeDTO);

        return communeRepository
            .findById(communeDTO.getId())
            .map(
                existingCommune -> {
                    communeMapper.partialUpdate(existingCommune, communeDTO);
                    return existingCommune;
                }
            )
            .flatMap(communeRepository::save)
            .map(communeMapper::toDto);
    }

    /**
     * Get all the communes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Communes");
        return communeRepository.findAllBy(pageable).map(communeMapper::toDto);
    }

    /**
     * Returns the number of communes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return communeRepository.count();
    }

    /**
     * Get one commune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CommuneDTO> findOne(Long id) {
        log.debug("Request to get Commune : {}", id);
        return communeRepository.findById(id).map(communeMapper::toDto);
    }

    /**
     * Delete the commune by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Commune : {}", id);
        return communeRepository.deleteById(id);
    }
}

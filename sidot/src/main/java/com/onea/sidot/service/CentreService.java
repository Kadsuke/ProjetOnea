package com.onea.sidot.service;

import com.onea.sidot.domain.Centre;
import com.onea.sidot.repository.CentreRepository;
import com.onea.sidot.service.dto.CentreDTO;
import com.onea.sidot.service.mapper.CentreMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Centre}.
 */
@Service
@Transactional
public class CentreService {

    private final Logger log = LoggerFactory.getLogger(CentreService.class);

    private final CentreRepository centreRepository;

    private final CentreMapper centreMapper;

    public CentreService(CentreRepository centreRepository, CentreMapper centreMapper) {
        this.centreRepository = centreRepository;
        this.centreMapper = centreMapper;
    }

    /**
     * Save a centre.
     *
     * @param centreDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CentreDTO> save(CentreDTO centreDTO) {
        log.debug("Request to save Centre : {}", centreDTO);
        return centreRepository.save(centreMapper.toEntity(centreDTO)).map(centreMapper::toDto);
    }

    /**
     * Partially update a centre.
     *
     * @param centreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CentreDTO> partialUpdate(CentreDTO centreDTO) {
        log.debug("Request to partially update Centre : {}", centreDTO);

        return centreRepository
            .findById(centreDTO.getId())
            .map(
                existingCentre -> {
                    centreMapper.partialUpdate(existingCentre, centreDTO);
                    return existingCentre;
                }
            )
            .flatMap(centreRepository::save)
            .map(centreMapper::toDto);
    }

    /**
     * Get all the centres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CentreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Centres");
        return centreRepository.findAllBy(pageable).map(centreMapper::toDto);
    }

    /**
     * Returns the number of centres available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return centreRepository.count();
    }

    /**
     * Get one centre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CentreDTO> findOne(Long id) {
        log.debug("Request to get Centre : {}", id);
        return centreRepository.findById(id).map(centreMapper::toDto);
    }

    /**
     * Delete the centre by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Centre : {}", id);
        return centreRepository.deleteById(id);
    }
}

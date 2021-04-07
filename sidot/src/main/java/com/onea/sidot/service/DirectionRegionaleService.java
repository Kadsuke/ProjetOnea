package com.onea.sidot.service;

import com.onea.sidot.domain.DirectionRegionale;
import com.onea.sidot.repository.DirectionRegionaleRepository;
import com.onea.sidot.service.dto.DirectionRegionaleDTO;
import com.onea.sidot.service.mapper.DirectionRegionaleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DirectionRegionale}.
 */
@Service
@Transactional
public class DirectionRegionaleService {

    private final Logger log = LoggerFactory.getLogger(DirectionRegionaleService.class);

    private final DirectionRegionaleRepository directionRegionaleRepository;

    private final DirectionRegionaleMapper directionRegionaleMapper;

    public DirectionRegionaleService(
        DirectionRegionaleRepository directionRegionaleRepository,
        DirectionRegionaleMapper directionRegionaleMapper
    ) {
        this.directionRegionaleRepository = directionRegionaleRepository;
        this.directionRegionaleMapper = directionRegionaleMapper;
    }

    /**
     * Save a directionRegionale.
     *
     * @param directionRegionaleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DirectionRegionaleDTO> save(DirectionRegionaleDTO directionRegionaleDTO) {
        log.debug("Request to save DirectionRegionale : {}", directionRegionaleDTO);
        return directionRegionaleRepository
            .save(directionRegionaleMapper.toEntity(directionRegionaleDTO))
            .map(directionRegionaleMapper::toDto);
    }

    /**
     * Partially update a directionRegionale.
     *
     * @param directionRegionaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DirectionRegionaleDTO> partialUpdate(DirectionRegionaleDTO directionRegionaleDTO) {
        log.debug("Request to partially update DirectionRegionale : {}", directionRegionaleDTO);

        return directionRegionaleRepository
            .findById(directionRegionaleDTO.getId())
            .map(
                existingDirectionRegionale -> {
                    directionRegionaleMapper.partialUpdate(existingDirectionRegionale, directionRegionaleDTO);
                    return existingDirectionRegionale;
                }
            )
            .flatMap(directionRegionaleRepository::save)
            .map(directionRegionaleMapper::toDto);
    }

    /**
     * Get all the directionRegionales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DirectionRegionaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DirectionRegionales");
        return directionRegionaleRepository.findAllBy(pageable).map(directionRegionaleMapper::toDto);
    }

    /**
     * Returns the number of directionRegionales available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return directionRegionaleRepository.count();
    }

    /**
     * Get one directionRegionale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DirectionRegionaleDTO> findOne(Long id) {
        log.debug("Request to get DirectionRegionale : {}", id);
        return directionRegionaleRepository.findById(id).map(directionRegionaleMapper::toDto);
    }

    /**
     * Delete the directionRegionale by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DirectionRegionale : {}", id);
        return directionRegionaleRepository.deleteById(id);
    }
}

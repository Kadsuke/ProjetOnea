package com.onea.sidot.service;

import com.onea.sidot.domain.Parcelle;
import com.onea.sidot.repository.ParcelleRepository;
import com.onea.sidot.service.dto.ParcelleDTO;
import com.onea.sidot.service.mapper.ParcelleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Parcelle}.
 */
@Service
@Transactional
public class ParcelleService {

    private final Logger log = LoggerFactory.getLogger(ParcelleService.class);

    private final ParcelleRepository parcelleRepository;

    private final ParcelleMapper parcelleMapper;

    public ParcelleService(ParcelleRepository parcelleRepository, ParcelleMapper parcelleMapper) {
        this.parcelleRepository = parcelleRepository;
        this.parcelleMapper = parcelleMapper;
    }

    /**
     * Save a parcelle.
     *
     * @param parcelleDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ParcelleDTO> save(ParcelleDTO parcelleDTO) {
        log.debug("Request to save Parcelle : {}", parcelleDTO);
        return parcelleRepository.save(parcelleMapper.toEntity(parcelleDTO)).map(parcelleMapper::toDto);
    }

    /**
     * Partially update a parcelle.
     *
     * @param parcelleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ParcelleDTO> partialUpdate(ParcelleDTO parcelleDTO) {
        log.debug("Request to partially update Parcelle : {}", parcelleDTO);

        return parcelleRepository
            .findById(parcelleDTO.getId())
            .map(
                existingParcelle -> {
                    parcelleMapper.partialUpdate(existingParcelle, parcelleDTO);
                    return existingParcelle;
                }
            )
            .flatMap(parcelleRepository::save)
            .map(parcelleMapper::toDto);
    }

    /**
     * Get all the parcelles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ParcelleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parcelles");
        return parcelleRepository.findAllBy(pageable).map(parcelleMapper::toDto);
    }

    /**
     * Returns the number of parcelles available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return parcelleRepository.count();
    }

    /**
     * Get one parcelle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ParcelleDTO> findOne(Long id) {
        log.debug("Request to get Parcelle : {}", id);
        return parcelleRepository.findById(id).map(parcelleMapper::toDto);
    }

    /**
     * Delete the parcelle by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Parcelle : {}", id);
        return parcelleRepository.deleteById(id);
    }
}

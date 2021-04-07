package com.onea.sidot.service;

import com.onea.sidot.domain.Localite;
import com.onea.sidot.repository.LocaliteRepository;
import com.onea.sidot.service.dto.LocaliteDTO;
import com.onea.sidot.service.mapper.LocaliteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Localite}.
 */
@Service
@Transactional
public class LocaliteService {

    private final Logger log = LoggerFactory.getLogger(LocaliteService.class);

    private final LocaliteRepository localiteRepository;

    private final LocaliteMapper localiteMapper;

    public LocaliteService(LocaliteRepository localiteRepository, LocaliteMapper localiteMapper) {
        this.localiteRepository = localiteRepository;
        this.localiteMapper = localiteMapper;
    }

    /**
     * Save a localite.
     *
     * @param localiteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<LocaliteDTO> save(LocaliteDTO localiteDTO) {
        log.debug("Request to save Localite : {}", localiteDTO);
        return localiteRepository.save(localiteMapper.toEntity(localiteDTO)).map(localiteMapper::toDto);
    }

    /**
     * Partially update a localite.
     *
     * @param localiteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LocaliteDTO> partialUpdate(LocaliteDTO localiteDTO) {
        log.debug("Request to partially update Localite : {}", localiteDTO);

        return localiteRepository
            .findById(localiteDTO.getId())
            .map(
                existingLocalite -> {
                    localiteMapper.partialUpdate(existingLocalite, localiteDTO);
                    return existingLocalite;
                }
            )
            .flatMap(localiteRepository::save)
            .map(localiteMapper::toDto);
    }

    /**
     * Get all the localites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LocaliteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Localites");
        return localiteRepository.findAllBy(pageable).map(localiteMapper::toDto);
    }

    /**
     * Returns the number of localites available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return localiteRepository.count();
    }

    /**
     * Get one localite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LocaliteDTO> findOne(Long id) {
        log.debug("Request to get Localite : {}", id);
        return localiteRepository.findById(id).map(localiteMapper::toDto);
    }

    /**
     * Delete the localite by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Localite : {}", id);
        return localiteRepository.deleteById(id);
    }
}

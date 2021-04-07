package com.onea.sidot.service;

import com.onea.sidot.domain.Secteur;
import com.onea.sidot.repository.SecteurRepository;
import com.onea.sidot.service.dto.SecteurDTO;
import com.onea.sidot.service.mapper.SecteurMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Secteur}.
 */
@Service
@Transactional
public class SecteurService {

    private final Logger log = LoggerFactory.getLogger(SecteurService.class);

    private final SecteurRepository secteurRepository;

    private final SecteurMapper secteurMapper;

    public SecteurService(SecteurRepository secteurRepository, SecteurMapper secteurMapper) {
        this.secteurRepository = secteurRepository;
        this.secteurMapper = secteurMapper;
    }

    /**
     * Save a secteur.
     *
     * @param secteurDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SecteurDTO> save(SecteurDTO secteurDTO) {
        log.debug("Request to save Secteur : {}", secteurDTO);
        return secteurRepository.save(secteurMapper.toEntity(secteurDTO)).map(secteurMapper::toDto);
    }

    /**
     * Partially update a secteur.
     *
     * @param secteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SecteurDTO> partialUpdate(SecteurDTO secteurDTO) {
        log.debug("Request to partially update Secteur : {}", secteurDTO);

        return secteurRepository
            .findById(secteurDTO.getId())
            .map(
                existingSecteur -> {
                    secteurMapper.partialUpdate(existingSecteur, secteurDTO);
                    return existingSecteur;
                }
            )
            .flatMap(secteurRepository::save)
            .map(secteurMapper::toDto);
    }

    /**
     * Get all the secteurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SecteurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Secteurs");
        return secteurRepository.findAllBy(pageable).map(secteurMapper::toDto);
    }

    /**
     * Returns the number of secteurs available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return secteurRepository.count();
    }

    /**
     * Get one secteur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SecteurDTO> findOne(Long id) {
        log.debug("Request to get Secteur : {}", id);
        return secteurRepository.findById(id).map(secteurMapper::toDto);
    }

    /**
     * Delete the secteur by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Secteur : {}", id);
        return secteurRepository.deleteById(id);
    }
}

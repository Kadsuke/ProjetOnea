package com.onea.referentiel.service;

import com.onea.referentiel.domain.Secteur;
import com.onea.referentiel.repository.SecteurRepository;
import com.onea.referentiel.service.dto.SecteurDTO;
import com.onea.referentiel.service.mapper.SecteurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public SecteurDTO save(SecteurDTO secteurDTO) {
        log.debug("Request to save Secteur : {}", secteurDTO);
        Secteur secteur = secteurMapper.toEntity(secteurDTO);
        secteur = secteurRepository.save(secteur);
        return secteurMapper.toDto(secteur);
    }

    /**
     * Partially update a secteur.
     *
     * @param secteurDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SecteurDTO> partialUpdate(SecteurDTO secteurDTO) {
        log.debug("Request to partially update Secteur : {}", secteurDTO);

        return secteurRepository
            .findById(secteurDTO.getId())
            .map(
                existingSecteur -> {
                    secteurMapper.partialUpdate(existingSecteur, secteurDTO);
                    return existingSecteur;
                }
            )
            .map(secteurRepository::save)
            .map(secteurMapper::toDto);
    }

    /**
     * Get all the secteurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SecteurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Secteurs");
        return secteurRepository.findAll(pageable).map(secteurMapper::toDto);
    }

    /**
     * Get one secteur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SecteurDTO> findOne(Long id) {
        log.debug("Request to get Secteur : {}", id);
        return secteurRepository.findById(id).map(secteurMapper::toDto);
    }

    /**
     * Delete the secteur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Secteur : {}", id);
        secteurRepository.deleteById(id);
    }
}

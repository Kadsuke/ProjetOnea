package com.sidot.gestioneau.service;

import com.sidot.gestioneau.domain.CentreRegroupement;
import com.sidot.gestioneau.repository.CentreRegroupementRepository;
import com.sidot.gestioneau.service.dto.CentreRegroupementDTO;
import com.sidot.gestioneau.service.mapper.CentreRegroupementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CentreRegroupement}.
 */
@Service
@Transactional
public class CentreRegroupementService {

    private final Logger log = LoggerFactory.getLogger(CentreRegroupementService.class);

    private final CentreRegroupementRepository centreRegroupementRepository;

    private final CentreRegroupementMapper centreRegroupementMapper;

    public CentreRegroupementService(
        CentreRegroupementRepository centreRegroupementRepository,
        CentreRegroupementMapper centreRegroupementMapper
    ) {
        this.centreRegroupementRepository = centreRegroupementRepository;
        this.centreRegroupementMapper = centreRegroupementMapper;
    }

    /**
     * Save a centreRegroupement.
     *
     * @param centreRegroupementDTO the entity to save.
     * @return the persisted entity.
     */
    public CentreRegroupementDTO save(CentreRegroupementDTO centreRegroupementDTO) {
        log.debug("Request to save CentreRegroupement : {}", centreRegroupementDTO);
        CentreRegroupement centreRegroupement = centreRegroupementMapper.toEntity(centreRegroupementDTO);
        centreRegroupement = centreRegroupementRepository.save(centreRegroupement);
        return centreRegroupementMapper.toDto(centreRegroupement);
    }

    /**
     * Partially update a centreRegroupement.
     *
     * @param centreRegroupementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CentreRegroupementDTO> partialUpdate(CentreRegroupementDTO centreRegroupementDTO) {
        log.debug("Request to partially update CentreRegroupement : {}", centreRegroupementDTO);

        return centreRegroupementRepository
            .findById(centreRegroupementDTO.getId())
            .map(
                existingCentreRegroupement -> {
                    centreRegroupementMapper.partialUpdate(existingCentreRegroupement, centreRegroupementDTO);
                    return existingCentreRegroupement;
                }
            )
            .map(centreRegroupementRepository::save)
            .map(centreRegroupementMapper::toDto);
    }

    /**
     * Get all the centreRegroupements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CentreRegroupementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CentreRegroupements");
        return centreRegroupementRepository.findAll(pageable).map(centreRegroupementMapper::toDto);
    }

    /**
     * Get one centreRegroupement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CentreRegroupementDTO> findOne(Long id) {
        log.debug("Request to get CentreRegroupement : {}", id);
        return centreRegroupementRepository.findById(id).map(centreRegroupementMapper::toDto);
    }

    /**
     * Delete the centreRegroupement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CentreRegroupement : {}", id);
        centreRegroupementRepository.deleteById(id);
    }
}

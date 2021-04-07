package com.onea.referentiel.service;

import com.onea.referentiel.domain.Parcelle;
import com.onea.referentiel.repository.ParcelleRepository;
import com.onea.referentiel.service.dto.ParcelleDTO;
import com.onea.referentiel.service.mapper.ParcelleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ParcelleDTO save(ParcelleDTO parcelleDTO) {
        log.debug("Request to save Parcelle : {}", parcelleDTO);
        Parcelle parcelle = parcelleMapper.toEntity(parcelleDTO);
        parcelle = parcelleRepository.save(parcelle);
        return parcelleMapper.toDto(parcelle);
    }

    /**
     * Partially update a parcelle.
     *
     * @param parcelleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ParcelleDTO> partialUpdate(ParcelleDTO parcelleDTO) {
        log.debug("Request to partially update Parcelle : {}", parcelleDTO);

        return parcelleRepository
            .findById(parcelleDTO.getId())
            .map(
                existingParcelle -> {
                    parcelleMapper.partialUpdate(existingParcelle, parcelleDTO);
                    return existingParcelle;
                }
            )
            .map(parcelleRepository::save)
            .map(parcelleMapper::toDto);
    }

    /**
     * Get all the parcelles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ParcelleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Parcelles");
        return parcelleRepository.findAll(pageable).map(parcelleMapper::toDto);
    }

    /**
     * Get one parcelle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ParcelleDTO> findOne(Long id) {
        log.debug("Request to get Parcelle : {}", id);
        return parcelleRepository.findById(id).map(parcelleMapper::toDto);
    }

    /**
     * Delete the parcelle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parcelle : {}", id);
        parcelleRepository.deleteById(id);
    }
}

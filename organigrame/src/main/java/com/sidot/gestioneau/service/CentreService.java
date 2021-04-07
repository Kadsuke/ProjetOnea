package com.sidot.gestioneau.service;

import com.sidot.gestioneau.domain.Centre;
import com.sidot.gestioneau.repository.CentreRepository;
import com.sidot.gestioneau.service.dto.CentreDTO;
import com.sidot.gestioneau.service.mapper.CentreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CentreDTO save(CentreDTO centreDTO) {
        log.debug("Request to save Centre : {}", centreDTO);
        Centre centre = centreMapper.toEntity(centreDTO);
        centre = centreRepository.save(centre);
        return centreMapper.toDto(centre);
    }

    /**
     * Partially update a centre.
     *
     * @param centreDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CentreDTO> partialUpdate(CentreDTO centreDTO) {
        log.debug("Request to partially update Centre : {}", centreDTO);

        return centreRepository
            .findById(centreDTO.getId())
            .map(
                existingCentre -> {
                    centreMapper.partialUpdate(existingCentre, centreDTO);
                    return existingCentre;
                }
            )
            .map(centreRepository::save)
            .map(centreMapper::toDto);
    }

    /**
     * Get all the centres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CentreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Centres");
        return centreRepository.findAll(pageable).map(centreMapper::toDto);
    }

    /**
     * Get one centre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CentreDTO> findOne(Long id) {
        log.debug("Request to get Centre : {}", id);
        return centreRepository.findById(id).map(centreMapper::toDto);
    }

    /**
     * Delete the centre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Centre : {}", id);
        centreRepository.deleteById(id);
    }
}

package com.sidot.gestioneau.service;

import com.sidot.gestioneau.domain.DirectionRegionale;
import com.sidot.gestioneau.repository.DirectionRegionaleRepository;
import com.sidot.gestioneau.service.dto.DirectionRegionaleDTO;
import com.sidot.gestioneau.service.mapper.DirectionRegionaleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public DirectionRegionaleDTO save(DirectionRegionaleDTO directionRegionaleDTO) {
        log.debug("Request to save DirectionRegionale : {}", directionRegionaleDTO);
        DirectionRegionale directionRegionale = directionRegionaleMapper.toEntity(directionRegionaleDTO);
        directionRegionale = directionRegionaleRepository.save(directionRegionale);
        return directionRegionaleMapper.toDto(directionRegionale);
    }

    /**
     * Partially update a directionRegionale.
     *
     * @param directionRegionaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DirectionRegionaleDTO> partialUpdate(DirectionRegionaleDTO directionRegionaleDTO) {
        log.debug("Request to partially update DirectionRegionale : {}", directionRegionaleDTO);

        return directionRegionaleRepository
            .findById(directionRegionaleDTO.getId())
            .map(
                existingDirectionRegionale -> {
                    directionRegionaleMapper.partialUpdate(existingDirectionRegionale, directionRegionaleDTO);
                    return existingDirectionRegionale;
                }
            )
            .map(directionRegionaleRepository::save)
            .map(directionRegionaleMapper::toDto);
    }

    /**
     * Get all the directionRegionales.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DirectionRegionaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DirectionRegionales");
        return directionRegionaleRepository.findAll(pageable).map(directionRegionaleMapper::toDto);
    }

    /**
     * Get one directionRegionale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DirectionRegionaleDTO> findOne(Long id) {
        log.debug("Request to get DirectionRegionale : {}", id);
        return directionRegionaleRepository.findById(id).map(directionRegionaleMapper::toDto);
    }

    /**
     * Delete the directionRegionale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DirectionRegionale : {}", id);
        directionRegionaleRepository.deleteById(id);
    }
}

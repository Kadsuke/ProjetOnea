package com.onea.referentiel.service;

import com.onea.referentiel.domain.Lot;
import com.onea.referentiel.repository.LotRepository;
import com.onea.referentiel.service.dto.LotDTO;
import com.onea.referentiel.service.mapper.LotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Lot}.
 */
@Service
@Transactional
public class LotService {

    private final Logger log = LoggerFactory.getLogger(LotService.class);

    private final LotRepository lotRepository;

    private final LotMapper lotMapper;

    public LotService(LotRepository lotRepository, LotMapper lotMapper) {
        this.lotRepository = lotRepository;
        this.lotMapper = lotMapper;
    }

    /**
     * Save a lot.
     *
     * @param lotDTO the entity to save.
     * @return the persisted entity.
     */
    public LotDTO save(LotDTO lotDTO) {
        log.debug("Request to save Lot : {}", lotDTO);
        Lot lot = lotMapper.toEntity(lotDTO);
        lot = lotRepository.save(lot);
        return lotMapper.toDto(lot);
    }

    /**
     * Partially update a lot.
     *
     * @param lotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LotDTO> partialUpdate(LotDTO lotDTO) {
        log.debug("Request to partially update Lot : {}", lotDTO);

        return lotRepository
            .findById(lotDTO.getId())
            .map(
                existingLot -> {
                    lotMapper.partialUpdate(existingLot, lotDTO);
                    return existingLot;
                }
            )
            .map(lotRepository::save)
            .map(lotMapper::toDto);
    }

    /**
     * Get all the lots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lots");
        return lotRepository.findAll(pageable).map(lotMapper::toDto);
    }

    /**
     * Get one lot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LotDTO> findOne(Long id) {
        log.debug("Request to get Lot : {}", id);
        return lotRepository.findById(id).map(lotMapper::toDto);
    }

    /**
     * Delete the lot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Lot : {}", id);
        lotRepository.deleteById(id);
    }
}

package com.onea.sidot.service;

import com.onea.sidot.domain.Lot;
import com.onea.sidot.repository.LotRepository;
import com.onea.sidot.service.dto.LotDTO;
import com.onea.sidot.service.mapper.LotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<LotDTO> save(LotDTO lotDTO) {
        log.debug("Request to save Lot : {}", lotDTO);
        return lotRepository.save(lotMapper.toEntity(lotDTO)).map(lotMapper::toDto);
    }

    /**
     * Partially update a lot.
     *
     * @param lotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<LotDTO> partialUpdate(LotDTO lotDTO) {
        log.debug("Request to partially update Lot : {}", lotDTO);

        return lotRepository
            .findById(lotDTO.getId())
            .map(
                existingLot -> {
                    lotMapper.partialUpdate(existingLot, lotDTO);
                    return existingLot;
                }
            )
            .flatMap(lotRepository::save)
            .map(lotMapper::toDto);
    }

    /**
     * Get all the lots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<LotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lots");
        return lotRepository.findAllBy(pageable).map(lotMapper::toDto);
    }

    /**
     * Returns the number of lots available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return lotRepository.count();
    }

    /**
     * Get one lot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<LotDTO> findOne(Long id) {
        log.debug("Request to get Lot : {}", id);
        return lotRepository.findById(id).map(lotMapper::toDto);
    }

    /**
     * Delete the lot by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Lot : {}", id);
        return lotRepository.deleteById(id);
    }
}

package com.onea.sidot.service;

import com.onea.sidot.domain.Site;
import com.onea.sidot.repository.SiteRepository;
import com.onea.sidot.service.dto.SiteDTO;
import com.onea.sidot.service.mapper.SiteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
@Transactional
public class SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteService.class);

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    public SiteService(SiteRepository siteRepository, SiteMapper siteMapper) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
    }

    /**
     * Save a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<SiteDTO> save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);
        return siteRepository.save(siteMapper.toEntity(siteDTO)).map(siteMapper::toDto);
    }

    /**
     * Partially update a site.
     *
     * @param siteDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<SiteDTO> partialUpdate(SiteDTO siteDTO) {
        log.debug("Request to partially update Site : {}", siteDTO);

        return siteRepository
            .findById(siteDTO.getId())
            .map(
                existingSite -> {
                    siteMapper.partialUpdate(existingSite, siteDTO);
                    return existingSite;
                }
            )
            .flatMap(siteRepository::save)
            .map(siteMapper::toDto);
    }

    /**
     * Get all the sites.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<SiteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sites");
        return siteRepository.findAllBy(pageable).map(siteMapper::toDto);
    }

    /**
     * Returns the number of sites available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return siteRepository.count();
    }

    /**
     * Get one site by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<SiteDTO> findOne(Long id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findById(id).map(siteMapper::toDto);
    }

    /**
     * Delete the site by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Site : {}", id);
        return siteRepository.deleteById(id);
    }
}

package com.onea.sidot.service;

import com.onea.sidot.domain.Province;
import com.onea.sidot.repository.ProvinceRepository;
import com.onea.sidot.service.dto.ProvinceDTO;
import com.onea.sidot.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceService.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceService(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ProvinceDTO> save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        return provinceRepository.save(provinceMapper.toEntity(provinceDTO)).map(provinceMapper::toDto);
    }

    /**
     * Partially update a province.
     *
     * @param provinceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ProvinceDTO> partialUpdate(ProvinceDTO provinceDTO) {
        log.debug("Request to partially update Province : {}", provinceDTO);

        return provinceRepository
            .findById(provinceDTO.getId())
            .map(
                existingProvince -> {
                    provinceMapper.partialUpdate(existingProvince, provinceDTO);
                    return existingProvince;
                }
            )
            .flatMap(provinceRepository::save)
            .map(provinceMapper::toDto);
    }

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAllBy(pageable).map(provinceMapper::toDto);
    }

    /**
     * Returns the number of provinces available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return provinceRepository.count();
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ProvinceDTO> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id).map(provinceMapper::toDto);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        return provinceRepository.deleteById(id);
    }
}

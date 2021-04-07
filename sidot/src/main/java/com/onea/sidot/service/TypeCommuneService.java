package com.onea.sidot.service;

import com.onea.sidot.domain.TypeCommune;
import com.onea.sidot.repository.TypeCommuneRepository;
import com.onea.sidot.service.dto.TypeCommuneDTO;
import com.onea.sidot.service.mapper.TypeCommuneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link TypeCommune}.
 */
@Service
@Transactional
public class TypeCommuneService {

    private final Logger log = LoggerFactory.getLogger(TypeCommuneService.class);

    private final TypeCommuneRepository typeCommuneRepository;

    private final TypeCommuneMapper typeCommuneMapper;

    public TypeCommuneService(TypeCommuneRepository typeCommuneRepository, TypeCommuneMapper typeCommuneMapper) {
        this.typeCommuneRepository = typeCommuneRepository;
        this.typeCommuneMapper = typeCommuneMapper;
    }

    /**
     * Save a typeCommune.
     *
     * @param typeCommuneDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<TypeCommuneDTO> save(TypeCommuneDTO typeCommuneDTO) {
        log.debug("Request to save TypeCommune : {}", typeCommuneDTO);
        return typeCommuneRepository.save(typeCommuneMapper.toEntity(typeCommuneDTO)).map(typeCommuneMapper::toDto);
    }

    /**
     * Partially update a typeCommune.
     *
     * @param typeCommuneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<TypeCommuneDTO> partialUpdate(TypeCommuneDTO typeCommuneDTO) {
        log.debug("Request to partially update TypeCommune : {}", typeCommuneDTO);

        return typeCommuneRepository
            .findById(typeCommuneDTO.getId())
            .map(
                existingTypeCommune -> {
                    typeCommuneMapper.partialUpdate(existingTypeCommune, typeCommuneDTO);
                    return existingTypeCommune;
                }
            )
            .flatMap(typeCommuneRepository::save)
            .map(typeCommuneMapper::toDto);
    }

    /**
     * Get all the typeCommunes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<TypeCommuneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TypeCommunes");
        return typeCommuneRepository.findAllBy(pageable).map(typeCommuneMapper::toDto);
    }

    /**
     * Returns the number of typeCommunes available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return typeCommuneRepository.count();
    }

    /**
     * Get one typeCommune by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<TypeCommuneDTO> findOne(Long id) {
        log.debug("Request to get TypeCommune : {}", id);
        return typeCommuneRepository.findById(id).map(typeCommuneMapper::toDto);
    }

    /**
     * Delete the typeCommune by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete TypeCommune : {}", id);
        return typeCommuneRepository.deleteById(id);
    }
}

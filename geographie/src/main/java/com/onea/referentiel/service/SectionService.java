package com.onea.referentiel.service;

import com.onea.referentiel.domain.Section;
import com.onea.referentiel.repository.SectionRepository;
import com.onea.referentiel.service.dto.SectionDTO;
import com.onea.referentiel.service.mapper.SectionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Section}.
 */
@Service
@Transactional
public class SectionService {

    private final Logger log = LoggerFactory.getLogger(SectionService.class);

    private final SectionRepository sectionRepository;

    private final SectionMapper sectionMapper;

    public SectionService(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }

    /**
     * Save a section.
     *
     * @param sectionDTO the entity to save.
     * @return the persisted entity.
     */
    public SectionDTO save(SectionDTO sectionDTO) {
        log.debug("Request to save Section : {}", sectionDTO);
        Section section = sectionMapper.toEntity(sectionDTO);
        section = sectionRepository.save(section);
        return sectionMapper.toDto(section);
    }

    /**
     * Partially update a section.
     *
     * @param sectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SectionDTO> partialUpdate(SectionDTO sectionDTO) {
        log.debug("Request to partially update Section : {}", sectionDTO);

        return sectionRepository
            .findById(sectionDTO.getId())
            .map(
                existingSection -> {
                    sectionMapper.partialUpdate(existingSection, sectionDTO);
                    return existingSection;
                }
            )
            .map(sectionRepository::save)
            .map(sectionMapper::toDto);
    }

    /**
     * Get all the sections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sections");
        return sectionRepository.findAll(pageable).map(sectionMapper::toDto);
    }

    /**
     * Get one section by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SectionDTO> findOne(Long id) {
        log.debug("Request to get Section : {}", id);
        return sectionRepository.findById(id).map(sectionMapper::toDto);
    }

    /**
     * Delete the section by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Section : {}", id);
        sectionRepository.deleteById(id);
    }
}

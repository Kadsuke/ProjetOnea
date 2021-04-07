package com.sidot.gestioneau.web.rest;

import com.sidot.gestioneau.repository.SiteRepository;
import com.sidot.gestioneau.service.SiteService;
import com.sidot.gestioneau.service.dto.SiteDTO;
import com.sidot.gestioneau.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sidot.gestioneau.domain.Site}.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);

    private static final String ENTITY_NAME = "organigrameSite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteService siteService;

    private final SiteRepository siteRepository;

    public SiteResource(SiteService siteService, SiteRepository siteRepository) {
        this.siteService = siteService;
        this.siteRepository = siteRepository;
    }

    /**
     * {@code POST  /sites} : Create a new site.
     *
     * @param siteDTO the siteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteDTO, or with status {@code 400 (Bad Request)} if the site has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sites")
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
        log.debug("REST request to save Site : {}", siteDTO);
        if (siteDTO.getId() != null) {
            throw new BadRequestAlertException("A new site cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SiteDTO result = siteService.save(siteDTO);
        return ResponseEntity
            .created(new URI("/api/sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sites/:id} : Updates an existing site.
     *
     * @param id the id of the siteDTO to save.
     * @param siteDTO the siteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteDTO,
     * or with status {@code 400 (Bad Request)} if the siteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sites/{id}")
    public ResponseEntity<SiteDTO> updateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Site : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SiteDTO result = siteService.save(siteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sites/:id} : Partial updates given fields of an existing site, field will ignore if it is null
     *
     * @param id the id of the siteDTO to save.
     * @param siteDTO the siteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteDTO,
     * or with status {@code 400 (Bad Request)} if the siteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the siteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sites/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SiteDTO> partialUpdateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Site partially : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteDTO> result = siteService.partialUpdate(siteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sites} : get all the sites.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sites in body.
     */
    @GetMapping("/sites")
    public ResponseEntity<List<SiteDTO>> getAllSites(Pageable pageable) {
        log.debug("REST request to get a page of Sites");
        Page<SiteDTO> page = siteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sites/:id} : get the "id" site.
     *
     * @param id the id of the siteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sites/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Optional<SiteDTO> siteDTO = siteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteDTO);
    }

    /**
     * {@code DELETE  /sites/:id} : delete the "id" site.
     *
     * @param id the id of the siteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sites/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long id) {
        log.debug("REST request to delete Site : {}", id);
        siteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

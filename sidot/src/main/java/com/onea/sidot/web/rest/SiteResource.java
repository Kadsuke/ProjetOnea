package com.onea.sidot.web.rest;

import com.onea.sidot.repository.SiteRepository;
import com.onea.sidot.service.SiteService;
import com.onea.sidot.service.dto.SiteDTO;
import com.onea.sidot.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.onea.sidot.domain.Site}.
 */
@RestController
@RequestMapping("/api")
public class SiteResource {

    private final Logger log = LoggerFactory.getLogger(SiteResource.class);

    private static final String ENTITY_NAME = "site";

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
    public Mono<ResponseEntity<SiteDTO>> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
        log.debug("REST request to save Site : {}", siteDTO);
        if (siteDTO.getId() != null) {
            throw new BadRequestAlertException("A new site cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return siteService
            .save(siteDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/sites/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
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
    public Mono<ResponseEntity<SiteDTO>> updateSite(
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

        return siteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return siteService
                        .save(siteDTO)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
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
    public Mono<ResponseEntity<SiteDTO>> partialUpdateSite(
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

        return siteRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<SiteDTO> result = siteService.partialUpdate(siteDTO);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /sites} : get all the sites.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sites in body.
     */
    @GetMapping("/sites")
    public Mono<ResponseEntity<List<SiteDTO>>> getAllSites(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Sites");
        return siteService
            .countAll()
            .zipWith(siteService.findAll(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /sites/:id} : get the "id" site.
     *
     * @param id the id of the siteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sites/{id}")
    public Mono<ResponseEntity<SiteDTO>> getSite(@PathVariable Long id) {
        log.debug("REST request to get Site : {}", id);
        Mono<SiteDTO> siteDTO = siteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteDTO);
    }

    /**
     * {@code DELETE  /sites/:id} : delete the "id" site.
     *
     * @param id the id of the siteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sites/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteSite(@PathVariable Long id) {
        log.debug("REST request to delete Site : {}", id);
        return siteService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}

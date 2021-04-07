package com.onea.sidot.web.rest;

import com.onea.sidot.repository.LotRepository;
import com.onea.sidot.service.LotService;
import com.onea.sidot.service.dto.LotDTO;
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
 * REST controller for managing {@link com.onea.sidot.domain.Lot}.
 */
@RestController
@RequestMapping("/api")
public class LotResource {

    private final Logger log = LoggerFactory.getLogger(LotResource.class);

    private static final String ENTITY_NAME = "lot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LotService lotService;

    private final LotRepository lotRepository;

    public LotResource(LotService lotService, LotRepository lotRepository) {
        this.lotService = lotService;
        this.lotRepository = lotRepository;
    }

    /**
     * {@code POST  /lots} : Create a new lot.
     *
     * @param lotDTO the lotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lotDTO, or with status {@code 400 (Bad Request)} if the lot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lots")
    public Mono<ResponseEntity<LotDTO>> createLot(@Valid @RequestBody LotDTO lotDTO) throws URISyntaxException {
        log.debug("REST request to save Lot : {}", lotDTO);
        if (lotDTO.getId() != null) {
            throw new BadRequestAlertException("A new lot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return lotService
            .save(lotDTO)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/lots/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /lots/:id} : Updates an existing lot.
     *
     * @param id the id of the lotDTO to save.
     * @param lotDTO the lotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotDTO,
     * or with status {@code 400 (Bad Request)} if the lotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lots/{id}")
    public Mono<ResponseEntity<LotDTO>> updateLot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LotDTO lotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Lot : {}, {}", id, lotDTO);
        if (lotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return lotRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return lotService
                        .save(lotDTO)
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
     * {@code PATCH  /lots/:id} : Partial updates given fields of an existing lot, field will ignore if it is null
     *
     * @param id the id of the lotDTO to save.
     * @param lotDTO the lotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lotDTO,
     * or with status {@code 400 (Bad Request)} if the lotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lots/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<LotDTO>> partialUpdateLot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LotDTO lotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lot partially : {}, {}", id, lotDTO);
        if (lotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return lotRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<LotDTO> result = lotService.partialUpdate(lotDTO);

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
     * {@code GET  /lots} : get all the lots.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lots in body.
     */
    @GetMapping("/lots")
    public Mono<ResponseEntity<List<LotDTO>>> getAllLots(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Lots");
        return lotService
            .countAll()
            .zipWith(lotService.findAll(pageable).collectList())
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
     * {@code GET  /lots/:id} : get the "id" lot.
     *
     * @param id the id of the lotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lots/{id}")
    public Mono<ResponseEntity<LotDTO>> getLot(@PathVariable Long id) {
        log.debug("REST request to get Lot : {}", id);
        Mono<LotDTO> lotDTO = lotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lotDTO);
    }

    /**
     * {@code DELETE  /lots/:id} : delete the "id" lot.
     *
     * @param id the id of the lotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lots/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteLot(@PathVariable Long id) {
        log.debug("REST request to delete Lot : {}", id);
        return lotService
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

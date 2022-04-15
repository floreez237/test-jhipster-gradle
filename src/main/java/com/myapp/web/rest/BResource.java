package com.myapp.web.rest;

import com.myapp.repository.BRepository;
import com.myapp.service.BService;
import com.myapp.service.dto.BDTO;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.myapp.domain.B}.
 */
@RestController
@RequestMapping("/api")
public class BResource {

    private final Logger log = LoggerFactory.getLogger(BResource.class);

    private static final String ENTITY_NAME = "b";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BService bService;

    private final BRepository bRepository;

    public BResource(BService bService, BRepository bRepository) {
        this.bService = bService;
        this.bRepository = bRepository;
    }

    /**
     * {@code POST  /bs} : Create a new b.
     *
     * @param bDTO the bDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bDTO, or with status {@code 400 (Bad Request)} if the b has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bs")
    public Mono<ResponseEntity<BDTO>> createB(@RequestBody BDTO bDTO) throws URISyntaxException {
        log.debug("REST request to save B : {}", bDTO);
        if (bDTO.getId() != null) {
            throw new BadRequestAlertException("A new b cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return bService
            .save(bDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/bs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /bs/:id} : Updates an existing b.
     *
     * @param id the id of the bDTO to save.
     * @param bDTO the bDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bDTO,
     * or with status {@code 400 (Bad Request)} if the bDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bs/{id}")
    public Mono<ResponseEntity<BDTO>> updateB(@PathVariable(value = "id", required = false) final Long id, @RequestBody BDTO bDTO)
        throws URISyntaxException {
        log.debug("REST request to update B : {}, {}", id, bDTO);
        if (bDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return bService
                    .save(bDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /bs/:id} : Partial updates given fields of an existing b, field will ignore if it is null
     *
     * @param id the id of the bDTO to save.
     * @param bDTO the bDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bDTO,
     * or with status {@code 400 (Bad Request)} if the bDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<BDTO>> partialUpdateB(@PathVariable(value = "id", required = false) final Long id, @RequestBody BDTO bDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update B partially : {}, {}", id, bDTO);
        if (bDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return bRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<BDTO> result = bService.partialUpdate(bDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /bs} : get all the bS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bS in body.
     */
    @GetMapping("/bs")
    public Mono<ResponseEntity<List<BDTO>>> getAllBS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of BS");
        return bService
            .countAll()
            .zipWith(bService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /bs/:id} : get the "id" b.
     *
     * @param id the id of the bDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bs/{id}")
    public Mono<ResponseEntity<BDTO>> getB(@PathVariable Long id) {
        log.debug("REST request to get B : {}", id);
        Mono<BDTO> bDTO = bService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bDTO);
    }

    /**
     * {@code DELETE  /bs/:id} : delete the "id" b.
     *
     * @param id the id of the bDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteB(@PathVariable Long id) {
        log.debug("REST request to delete B : {}", id);
        return bService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}

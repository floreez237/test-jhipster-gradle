package com.myapp.web.rest;

import com.myapp.repository.DRepository;
import com.myapp.service.DService;
import com.myapp.service.dto.DDTO;
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
 * REST controller for managing {@link com.myapp.domain.D}.
 */
@RestController
@RequestMapping("/api")
public class DResource {

    private final Logger log = LoggerFactory.getLogger(DResource.class);

    private static final String ENTITY_NAME = "d";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DService dService;

    private final DRepository dRepository;

    public DResource(DService dService, DRepository dRepository) {
        this.dService = dService;
        this.dRepository = dRepository;
    }

    /**
     * {@code POST  /ds} : Create a new d.
     *
     * @param dDTO the dDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dDTO, or with status {@code 400 (Bad Request)} if the d has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ds")
    public Mono<ResponseEntity<DDTO>> createD(@RequestBody DDTO dDTO) throws URISyntaxException {
        log.debug("REST request to save D : {}", dDTO);
        if (dDTO.getId() != null) {
            throw new BadRequestAlertException("A new d cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dService
            .save(dDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/ds/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /ds/:id} : Updates an existing d.
     *
     * @param id the id of the dDTO to save.
     * @param dDTO the dDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dDTO,
     * or with status {@code 400 (Bad Request)} if the dDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ds/{id}")
    public Mono<ResponseEntity<DDTO>> updateD(@PathVariable(value = "id", required = false) final Long id, @RequestBody DDTO dDTO)
        throws URISyntaxException {
        log.debug("REST request to update D : {}, {}", id, dDTO);
        if (dDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dService
                    .save(dDTO)
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
     * {@code PATCH  /ds/:id} : Partial updates given fields of an existing d, field will ignore if it is null
     *
     * @param id the id of the dDTO to save.
     * @param dDTO the dDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dDTO,
     * or with status {@code 400 (Bad Request)} if the dDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ds/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DDTO>> partialUpdateD(@PathVariable(value = "id", required = false) final Long id, @RequestBody DDTO dDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update D partially : {}, {}", id, dDTO);
        if (dDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DDTO> result = dService.partialUpdate(dDTO);

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
     * {@code GET  /ds} : get all the dS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dS in body.
     */
    @GetMapping("/ds")
    public Mono<ResponseEntity<List<DDTO>>> getAllDS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DS");
        return dService
            .countAll()
            .zipWith(dService.findAll(pageable).collectList())
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
     * {@code GET  /ds/:id} : get the "id" d.
     *
     * @param id the id of the dDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ds/{id}")
    public Mono<ResponseEntity<DDTO>> getD(@PathVariable Long id) {
        log.debug("REST request to get D : {}", id);
        Mono<DDTO> dDTO = dService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dDTO);
    }

    /**
     * {@code DELETE  /ds/:id} : delete the "id" d.
     *
     * @param id the id of the dDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ds/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteD(@PathVariable Long id) {
        log.debug("REST request to delete D : {}", id);
        return dService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}

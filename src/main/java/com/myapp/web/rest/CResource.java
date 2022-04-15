package com.myapp.web.rest;

import com.myapp.repository.CRepository;
import com.myapp.service.CService;
import com.myapp.service.dto.CDTO;
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
 * REST controller for managing {@link com.myapp.domain.C}.
 */
@RestController
@RequestMapping("/api")
public class CResource {

    private final Logger log = LoggerFactory.getLogger(CResource.class);

    private static final String ENTITY_NAME = "c";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CService cService;

    private final CRepository cRepository;

    public CResource(CService cService, CRepository cRepository) {
        this.cService = cService;
        this.cRepository = cRepository;
    }

    /**
     * {@code POST  /cs} : Create a new c.
     *
     * @param cDTO the cDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cDTO, or with status {@code 400 (Bad Request)} if the c has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cs")
    public Mono<ResponseEntity<CDTO>> createC(@RequestBody CDTO cDTO) throws URISyntaxException {
        log.debug("REST request to save C : {}", cDTO);
        if (cDTO.getId() != null) {
            throw new BadRequestAlertException("A new c cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cService
            .save(cDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cs/:id} : Updates an existing c.
     *
     * @param id the id of the cDTO to save.
     * @param cDTO the cDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cDTO,
     * or with status {@code 400 (Bad Request)} if the cDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cs/{id}")
    public Mono<ResponseEntity<CDTO>> updateC(@PathVariable(value = "id", required = false) final Long id, @RequestBody CDTO cDTO)
        throws URISyntaxException {
        log.debug("REST request to update C : {}, {}", id, cDTO);
        if (cDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cService
                    .save(cDTO)
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
     * {@code PATCH  /cs/:id} : Partial updates given fields of an existing c, field will ignore if it is null
     *
     * @param id the id of the cDTO to save.
     * @param cDTO the cDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cDTO,
     * or with status {@code 400 (Bad Request)} if the cDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CDTO>> partialUpdateC(@PathVariable(value = "id", required = false) final Long id, @RequestBody CDTO cDTO)
        throws URISyntaxException {
        log.debug("REST request to partial update C partially : {}, {}", id, cDTO);
        if (cDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CDTO> result = cService.partialUpdate(cDTO);

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
     * {@code GET  /cs} : get all the cS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cS in body.
     */
    @GetMapping("/cs")
    public Mono<ResponseEntity<List<CDTO>>> getAllCS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of CS");
        return cService
            .countAll()
            .zipWith(cService.findAll(pageable).collectList())
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
     * {@code GET  /cs/:id} : get the "id" c.
     *
     * @param id the id of the cDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cs/{id}")
    public Mono<ResponseEntity<CDTO>> getC(@PathVariable Long id) {
        log.debug("REST request to get C : {}", id);
        Mono<CDTO> cDTO = cService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cDTO);
    }

    /**
     * {@code DELETE  /cs/:id} : delete the "id" c.
     *
     * @param id the id of the cDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cs/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteC(@PathVariable Long id) {
        log.debug("REST request to delete C : {}", id);
        return cService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}

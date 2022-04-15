package com.myapp.web.rest;

import com.myapp.repository.ARepository;
import com.myapp.service.AService;
import com.myapp.service.dto.ADTO;
import com.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.myapp.domain.A}.
 */
@RestController
@RequestMapping("/api")
public class AResource {

    private final Logger log = LoggerFactory.getLogger(AResource.class);

    private static final String ENTITY_NAME = "a";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AService aService;

    private final ARepository aRepository;

    public AResource(AService aService, ARepository aRepository) {
        this.aService = aService;
        this.aRepository = aRepository;
    }

    /**
     * {@code POST  /as} : Create a new a.
     *
     * @param aDTO the aDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aDTO, or with status {@code 400 (Bad Request)} if the a has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/as")
    public Mono<ResponseEntity<ADTO>> createA(@Valid @RequestBody ADTO aDTO) throws URISyntaxException {
        log.debug("REST request to save A : {}", aDTO);
        if (aDTO.getId() != null) {
            throw new BadRequestAlertException("A new a cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return aService
            .save(aDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/as/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /as/:id} : Updates an existing a.
     *
     * @param id the id of the aDTO to save.
     * @param aDTO the aDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aDTO,
     * or with status {@code 400 (Bad Request)} if the aDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/as/{id}")
    public Mono<ResponseEntity<ADTO>> updateA(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody ADTO aDTO)
        throws URISyntaxException {
        log.debug("REST request to update A : {}, {}", id, aDTO);
        if (aDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return aService
                    .save(aDTO)
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
     * {@code PATCH  /as/:id} : Partial updates given fields of an existing a, field will ignore if it is null
     *
     * @param id the id of the aDTO to save.
     * @param aDTO the aDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aDTO,
     * or with status {@code 400 (Bad Request)} if the aDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/as/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ADTO>> partialUpdateA(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ADTO aDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update A partially : {}, {}", id, aDTO);
        if (aDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return aRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ADTO> result = aService.partialUpdate(aDTO);

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
     * {@code GET  /as} : get all the aS.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aS in body.
     */
    @GetMapping("/as")
    public Mono<ResponseEntity<List<ADTO>>> getAllAS(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of AS");
        return aService
            .countAll()
            .zipWith(aService.findAll(pageable).collectList())
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
     * {@code GET  /as/:id} : get the "id" a.
     *
     * @param id the id of the aDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/as/{id}")
    public Mono<ResponseEntity<ADTO>> getA(@PathVariable Long id) {
        log.debug("REST request to get A : {}", id);
        Mono<ADTO> aDTO = aService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aDTO);
    }

    /**
     * {@code DELETE  /as/:id} : delete the "id" a.
     *
     * @param id the id of the aDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/as/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteA(@PathVariable Long id) {
        log.debug("REST request to delete A : {}", id);
        return aService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}

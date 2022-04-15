package com.myapp.service;

import com.myapp.domain.D;
import com.myapp.repository.DRepository;
import com.myapp.service.dto.DDTO;
import com.myapp.service.mapper.DMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link D}.
 */
@Service
@Transactional
public class DService {

    private final Logger log = LoggerFactory.getLogger(DService.class);

    private final DRepository dRepository;

    private final DMapper dMapper;

    public DService(DRepository dRepository, DMapper dMapper) {
        this.dRepository = dRepository;
        this.dMapper = dMapper;
    }

    /**
     * Save a d.
     *
     * @param dDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<DDTO> save(DDTO dDTO) {
        log.debug("Request to save D : {}", dDTO);
        return dRepository.save(dMapper.toEntity(dDTO)).map(dMapper::toDto);
    }

    /**
     * Partially update a d.
     *
     * @param dDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DDTO> partialUpdate(DDTO dDTO) {
        log.debug("Request to partially update D : {}", dDTO);

        return dRepository
            .findById(dDTO.getId())
            .map(existingD -> {
                dMapper.partialUpdate(existingD, dDTO);

                return existingD;
            })
            .flatMap(dRepository::save)
            .map(dMapper::toDto);
    }

    /**
     * Get all the dS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DS");
        return dRepository.findAllBy(pageable).map(dMapper::toDto);
    }

    /**
     * Returns the number of dS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return dRepository.count();
    }

    /**
     * Get one d by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DDTO> findOne(Long id) {
        log.debug("Request to get D : {}", id);
        return dRepository.findById(id).map(dMapper::toDto);
    }

    /**
     * Delete the d by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete D : {}", id);
        return dRepository.deleteById(id);
    }
}

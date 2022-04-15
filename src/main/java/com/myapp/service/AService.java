package com.myapp.service;

import com.myapp.domain.A;
import com.myapp.repository.ARepository;
import com.myapp.service.dto.ADTO;
import com.myapp.service.mapper.AMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link A}.
 */
@Service
@Transactional
public class AService {

    private final Logger log = LoggerFactory.getLogger(AService.class);

    private final ARepository aRepository;

    private final AMapper aMapper;

    public AService(ARepository aRepository, AMapper aMapper) {
        this.aRepository = aRepository;
        this.aMapper = aMapper;
    }

    /**
     * Save a a.
     *
     * @param aDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<ADTO> save(ADTO aDTO) {
        log.debug("Request to save A : {}", aDTO);
        return aRepository.save(aMapper.toEntity(aDTO)).map(aMapper::toDto);
    }

    /**
     * Partially update a a.
     *
     * @param aDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<ADTO> partialUpdate(ADTO aDTO) {
        log.debug("Request to partially update A : {}", aDTO);

        return aRepository
            .findById(aDTO.getId())
            .map(existingA -> {
                aMapper.partialUpdate(existingA, aDTO);

                return existingA;
            })
            .flatMap(aRepository::save)
            .map(aMapper::toDto);
    }

    /**
     * Get all the aS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<ADTO> findAll(Pageable pageable) {
        log.debug("Request to get all AS");
        return aRepository.findAllBy(pageable).map(aMapper::toDto);
    }

    /**
     * Returns the number of aS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return aRepository.count();
    }

    /**
     * Get one a by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<ADTO> findOne(Long id) {
        log.debug("Request to get A : {}", id);
        return aRepository.findById(id).map(aMapper::toDto);
    }

    /**
     * Delete the a by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete A : {}", id);
        return aRepository.deleteById(id);
    }
}

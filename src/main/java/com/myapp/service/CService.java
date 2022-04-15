package com.myapp.service;

import com.myapp.domain.C;
import com.myapp.repository.CRepository;
import com.myapp.service.dto.CDTO;
import com.myapp.service.mapper.CMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link C}.
 */
@Service
@Transactional
public class CService {

    private final Logger log = LoggerFactory.getLogger(CService.class);

    private final CRepository cRepository;

    private final CMapper cMapper;

    public CService(CRepository cRepository, CMapper cMapper) {
        this.cRepository = cRepository;
        this.cMapper = cMapper;
    }

    /**
     * Save a c.
     *
     * @param cDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<CDTO> save(CDTO cDTO) {
        log.debug("Request to save C : {}", cDTO);
        return cRepository.save(cMapper.toEntity(cDTO)).map(cMapper::toDto);
    }

    /**
     * Partially update a c.
     *
     * @param cDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<CDTO> partialUpdate(CDTO cDTO) {
        log.debug("Request to partially update C : {}", cDTO);

        return cRepository
            .findById(cDTO.getId())
            .map(existingC -> {
                cMapper.partialUpdate(existingC, cDTO);

                return existingC;
            })
            .flatMap(cRepository::save)
            .map(cMapper::toDto);
    }

    /**
     * Get all the cS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<CDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CS");
        return cRepository.findAllBy(pageable).map(cMapper::toDto);
    }

    /**
     * Returns the number of cS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return cRepository.count();
    }

    /**
     * Get one c by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<CDTO> findOne(Long id) {
        log.debug("Request to get C : {}", id);
        return cRepository.findById(id).map(cMapper::toDto);
    }

    /**
     * Delete the c by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete C : {}", id);
        return cRepository.deleteById(id);
    }
}

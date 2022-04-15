package com.myapp.service;

import com.myapp.domain.B;
import com.myapp.repository.BRepository;
import com.myapp.service.dto.BDTO;
import com.myapp.service.mapper.BMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link B}.
 */
@Service
@Transactional
public class BService {

    private final Logger log = LoggerFactory.getLogger(BService.class);

    private final BRepository bRepository;

    private final BMapper bMapper;

    public BService(BRepository bRepository, BMapper bMapper) {
        this.bRepository = bRepository;
        this.bMapper = bMapper;
    }

    /**
     * Save a b.
     *
     * @param bDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<BDTO> save(BDTO bDTO) {
        log.debug("Request to save B : {}", bDTO);
        return bRepository.save(bMapper.toEntity(bDTO)).map(bMapper::toDto);
    }

    /**
     * Partially update a b.
     *
     * @param bDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<BDTO> partialUpdate(BDTO bDTO) {
        log.debug("Request to partially update B : {}", bDTO);

        return bRepository
            .findById(bDTO.getId())
            .map(existingB -> {
                bMapper.partialUpdate(existingB, bDTO);

                return existingB;
            })
            .flatMap(bRepository::save)
            .map(bMapper::toDto);
    }

    /**
     * Get all the bS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<BDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BS");
        return bRepository.findAllBy(pageable).map(bMapper::toDto);
    }

    /**
     * Returns the number of bS available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return bRepository.count();
    }

    /**
     * Get one b by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<BDTO> findOne(Long id) {
        log.debug("Request to get B : {}", id);
        return bRepository.findById(id).map(bMapper::toDto);
    }

    /**
     * Delete the b by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete B : {}", id);
        return bRepository.deleteById(id);
    }
}

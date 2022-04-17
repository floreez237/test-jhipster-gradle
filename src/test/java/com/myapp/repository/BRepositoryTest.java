package com.myapp.repository;

import com.myapp.config.LiquibaseConfiguration;
import com.myapp.domain.A;
import com.myapp.domain.B;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@CustomR2dbcTests
class BRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(LiquibaseConfiguration.class);

    @Autowired
    private ARepository aRepository;

    @Autowired
    private BRepository bRepository;

    @Value("${spring.liquibase.change-log}")
    private String tests;

    @BeforeEach
    void setUp() {
        A a = new A();
        a.setTest("testing");
        a = aRepository.save(a).block();
        log.info("A ID {}", a.getId());
        B b = new B();
        b.setAId(a.getId());
        bRepository.save(b).block();
    }

    @Test
    void findByA() {
        Mono<B> byA = bRepository.findByA(1L).single();

        StepVerifier
            .create(byA)
            /* .assertNext(b -> {
                 assertEquals(1L, b.getAId());
             })*/.expectNextCount(1L)
            .verifyComplete();
    }

    @Test
    void findAllWhereAIsNull() {}
}

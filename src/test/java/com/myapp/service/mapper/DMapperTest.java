package com.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DMapperTest {

    private DMapper dMapper;

    @BeforeEach
    public void setUp() {
        dMapper = new DMapperImpl();
    }
}

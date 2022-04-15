package com.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BMapperTest {

    private BMapper bMapper;

    @BeforeEach
    public void setUp() {
        bMapper = new BMapperImpl();
    }
}

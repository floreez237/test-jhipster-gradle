package com.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AMapperTest {

    private AMapper aMapper;

    @BeforeEach
    public void setUp() {
        aMapper = new AMapperImpl();
    }
}

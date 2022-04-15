package com.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CMapperTest {

    private CMapper cMapper;

    @BeforeEach
    public void setUp() {
        cMapper = new CMapperImpl();
    }
}

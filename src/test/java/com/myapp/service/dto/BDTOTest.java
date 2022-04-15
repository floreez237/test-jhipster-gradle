package com.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BDTO.class);
        BDTO bDTO1 = new BDTO();
        bDTO1.setId(1L);
        BDTO bDTO2 = new BDTO();
        assertThat(bDTO1).isNotEqualTo(bDTO2);
        bDTO2.setId(bDTO1.getId());
        assertThat(bDTO1).isEqualTo(bDTO2);
        bDTO2.setId(2L);
        assertThat(bDTO1).isNotEqualTo(bDTO2);
        bDTO1.setId(null);
        assertThat(bDTO1).isNotEqualTo(bDTO2);
    }
}

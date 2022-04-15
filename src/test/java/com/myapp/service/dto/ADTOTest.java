package com.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ADTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ADTO.class);
        ADTO aDTO1 = new ADTO();
        aDTO1.setId(1L);
        ADTO aDTO2 = new ADTO();
        assertThat(aDTO1).isNotEqualTo(aDTO2);
        aDTO2.setId(aDTO1.getId());
        assertThat(aDTO1).isEqualTo(aDTO2);
        aDTO2.setId(2L);
        assertThat(aDTO1).isNotEqualTo(aDTO2);
        aDTO1.setId(null);
        assertThat(aDTO1).isNotEqualTo(aDTO2);
    }
}

package com.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myapp.domain.A} entity.
 */
public class ADTO implements Serializable {

    private Long id;

    @NotNull(message = "must not be null")
    private String test;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ADTO)) {
            return false;
        }

        ADTO aDTO = (ADTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ADTO{" +
            "id=" + getId() +
            ", test='" + getTest() + "'" +
            "}";
    }
}

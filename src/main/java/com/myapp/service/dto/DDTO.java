package com.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.domain.D} entity.
 */
public class DDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DDTO)) {
            return false;
        }

        DDTO dDTO = (DDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DDTO{" +
            "id=" + getId() +
            "}";
    }
}

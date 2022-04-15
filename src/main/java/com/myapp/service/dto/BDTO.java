package com.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.domain.B} entity.
 */
public class BDTO implements Serializable {

    private Long id;

    private ADTO a;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ADTO getA() {
        return a;
    }

    public void setA(ADTO a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BDTO)) {
            return false;
        }

        BDTO bDTO = (BDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BDTO{" +
            "id=" + getId() +
            ", a=" + getA() +
            "}";
    }
}

package com.myapp.service.mapper;

import com.myapp.domain.B;
import com.myapp.service.dto.BDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link B} and its DTO {@link BDTO}.
 */
@Mapper(componentModel = "spring", uses = { AMapper.class })
public interface BMapper extends EntityMapper<BDTO, B> {
    @Mapping(target = "a", source = "a", qualifiedByName = "id")
    BDTO toDto(B s);
}

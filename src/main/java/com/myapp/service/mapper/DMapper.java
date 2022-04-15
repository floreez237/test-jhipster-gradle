package com.myapp.service.mapper;

import com.myapp.domain.D;
import com.myapp.service.dto.DDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link D} and its DTO {@link DDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DMapper extends EntityMapper<DDTO, D> {}

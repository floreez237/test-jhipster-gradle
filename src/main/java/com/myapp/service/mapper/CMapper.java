package com.myapp.service.mapper;

import com.myapp.domain.C;
import com.myapp.service.dto.CDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link C} and its DTO {@link CDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CMapper extends EntityMapper<CDTO, C> {}

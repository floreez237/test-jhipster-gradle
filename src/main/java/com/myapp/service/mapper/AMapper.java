package com.myapp.service.mapper;

import com.myapp.domain.A;
import com.myapp.service.dto.ADTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link A} and its DTO {@link ADTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AMapper extends EntityMapper<ADTO, A> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ADTO toDtoId(A a);
}

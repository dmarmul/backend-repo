package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.user.RealtorDto;
import com.example.backend.model.Realtor;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RealtorMapper {
    RealtorDto toDto(Realtor realtor);
}

package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.NeighborhoodCartDto;
import com.example.backend.dto.NeighborhoodDto;
import com.example.backend.model.Neighborhood;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface NeighborhoodMapper {
    NeighborhoodCartDto toCartDto(Neighborhood neighborhood);

    NeighborhoodDto toDto(Neighborhood neighborhood);
}

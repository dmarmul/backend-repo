package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.model.House;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface HouseMapper {
    HouseDto toDto(House house);

    HouseCartDto toHouseCartDto(House house);
}

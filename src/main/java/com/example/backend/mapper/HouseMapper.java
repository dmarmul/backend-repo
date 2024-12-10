package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.house.HouseDto;
import com.example.backend.model.Feature;
import com.example.backend.model.House;
import com.example.backend.model.PhotoLink;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface HouseMapper {
    @Mapping(target = "photoLinks", ignore = true)
    @Mapping(target = "features", ignore = true)
    HouseDto toDto(House house);

    @Mapping(target = "photoUrl", ignore = true)
    HouseCartDto toHouseCartDto(House house);

    @AfterMapping
    default void mapPhotoLinksToHouseCartDto(House house, @MappingTarget HouseCartDto dto) {
        if (house.getPhotoLinks() != null) {
            dto.setPhotoUrl(
                    house.getPhotoLinks().stream()
                            .map(PhotoLink::getPhotoLink)
                            .findFirst()
                            .orElse("https://smart-estate-backend.s3.amazonaws.com/standatrKv.jpg")
            );
        }
    }

    @AfterMapping
    default void mapPhotoLinksToHouseDto(House house, @MappingTarget HouseDto dto) {
        if (house.getPhotoLinks() != null) {
            dto.setPhotoLinks(
                    house.getPhotoLinks().stream()
                            .map(PhotoLink::getPhotoLink)
                            .collect(Collectors.toSet())
            );
        }
        if (house.getFeatures() != null) {
            dto.setFeatures(
                    house.getFeatures().stream()
                            .map(Feature::getAuthority)
                            .collect(Collectors.toSet())
            );
        }
    }
}

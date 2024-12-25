package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.house.ContactedHouseCartDto;
import com.example.backend.model.UserContact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {HouseMapper.class, RealtorMapper.class})
public interface UserContactMapper {
    @Mapping(target = "houseCartDto", source = "house")
    @Mapping(target = "houseCartDto.price", source = "house.price")
    @Mapping(target = "realtorCartDto", source = "house.realtor")
    ContactedHouseCartDto toContactedHouseCardDto(UserContact userContact);
}

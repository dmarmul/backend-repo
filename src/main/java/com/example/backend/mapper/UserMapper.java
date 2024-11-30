package com.example.backend.mapper;

import com.example.backend.config.MapperConfig;
import com.example.backend.dto.UserRegistrationRequestDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userDto);
}

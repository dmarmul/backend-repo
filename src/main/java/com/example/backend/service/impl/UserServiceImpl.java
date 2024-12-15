package com.example.backend.service.impl;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.user.UserLoginRequestDto;
import com.example.backend.dto.user.UserRegistrationRequestDto;
import com.example.backend.dto.user.UserResponseDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.RegistrationException;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.House;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthenticationService;
import com.example.backend.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final AuthenticationService authenticationService;
    private final HouseMapper houseMapper;
    private final HouseServiceImpl houseService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "User " + requestDto.getEmail() + " already exist");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleRepository.findByRole(Role.RoleType.ROLE_USER)));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto authenticate(UserLoginRequestDto requestDto) {
        String token = authenticationService.authenticate(
                requestDto.email(), requestDto.password());
        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + requestDto.email()));
        UserResponseDto dto = userMapper.toDto(user);
        dto.setToken(token);
        return dto;
    }

    @Override
    public List<HouseCartDto> findAllLikedHouses(User user, Pageable pageable) {
        Set<House> likedHouses = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + user.getEmail()))
                .getLikedHouses();

        return likedHouses.stream()
                .peek(house ->
                        house.getPhotoLinks()
                                .forEach(photoLink -> photoLink.setPhotoLink(
                                        houseService.setUrl(photoLink.getPhotoLink()))
                                )
                )
                .map(houseMapper::toHouseCartDto)
                .peek(houseCartDto -> houseCartDto.setIsLiked(true))
                .skip((long) pageable.getPageNumber() * pageable.getPageSize())
                .limit(pageable.getPageSize())
                .toList();
    }
}

package com.example.backend.service.impl;

import com.example.backend.dto.house.ContactedHousePageDto;
import com.example.backend.dto.house.LickedHousePageDto;
import com.example.backend.dto.user.UserLoginRequestDto;
import com.example.backend.dto.user.UserRegistrationRequestDto;
import com.example.backend.dto.user.UserResponseDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.exception.RegistrationException;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.mapper.UserContactMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.House;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserContact;
import com.example.backend.repository.CheckUserRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.AuthenticationService;
import com.example.backend.service.UserService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final int USER_CONTACTS_PAGE_SIZE = 2;
    private static final int LIKED_HOUSES_PAGE_SIZE = 4;
    private final AuthenticationService authenticationService;
    private final UserContactMapper contactMapper;
    private final HouseMapper houseMapper;
    private final HouseServiceImpl houseService;
    private final CheckUserRepository checkUserRepository;
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
        if (!checkUserRepository.existsByEmailAndCheckedNumber(requestDto.getEmail(),
                requestDto.getVerificationCode())) {
            throw new RegistrationException(
                    "Code " + requestDto.getVerificationCode() + " is not match");
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
    public LickedHousePageDto findAllLikedHouses(User user, Pageable pageable) {
        if (pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageable.getPageNumber() - 1, LIKED_HOUSES_PAGE_SIZE);
        }

        Set<House> likedHouses = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + user.getEmail()))
                .getLikedHouses();
        LickedHousePageDto lickedHousePageDto = new LickedHousePageDto();
        int maxHouseNumber = likedHouses.size();
        lickedHousePageDto.setMaxPageNumber(
                maxHouseNumber > 0 && maxHouseNumber < LIKED_HOUSES_PAGE_SIZE
                ? 1 : (maxHouseNumber / LIKED_HOUSES_PAGE_SIZE % LIKED_HOUSES_PAGE_SIZE == 0
                ? maxHouseNumber / LIKED_HOUSES_PAGE_SIZE
                : (maxHouseNumber / LIKED_HOUSES_PAGE_SIZE) + 1));
        lickedHousePageDto.setHouseCartDtoList(likedHouses.stream()
                .skip((long) pageable.getPageNumber() * LIKED_HOUSES_PAGE_SIZE)
                .limit(LIKED_HOUSES_PAGE_SIZE)
                .peek(house ->
                        house.getPhotoLinks()
                                .forEach(photoLink -> photoLink.setPhotoLink(
                                        houseService.setUrl(photoLink.getPhotoLink()))
                                )
                )
                .map(houseMapper::toHouseCartDto)
                .peek(houseCartDto -> houseCartDto.setIsLiked(true))
                .toList());
        return lickedHousePageDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ContactedHousePageDto findAllUserContacts(User user, Pageable pageable) {
        if (pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageable.getPageNumber() - 1, USER_CONTACTS_PAGE_SIZE);
        }

        User savedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + user.getEmail()));

        List<UserContact> userContacts = savedUser.getContacts();
        ContactedHousePageDto contactedHousePageDto = new ContactedHousePageDto();
        int maxHouseNumber = userContacts.size();
        contactedHousePageDto.setMaxPageNumber(
                maxHouseNumber > 0 && maxHouseNumber < USER_CONTACTS_PAGE_SIZE
                ? 1 : (maxHouseNumber / USER_CONTACTS_PAGE_SIZE % USER_CONTACTS_PAGE_SIZE == 0
                        ? maxHouseNumber / USER_CONTACTS_PAGE_SIZE
                        : (maxHouseNumber / USER_CONTACTS_PAGE_SIZE) + 1));
        contactedHousePageDto.setHouseCartDtoList(userContacts.stream()
                .skip((long) pageable.getPageNumber() * USER_CONTACTS_PAGE_SIZE)
                .limit(USER_CONTACTS_PAGE_SIZE)
                .peek(userContact -> userContact.getHouse().getPhotoLinks()
                        .forEach(photoLink -> photoLink.setPhotoLink(
                                houseService.setUrl(photoLink.getPhotoLink()))
                        )
                )
                .map(contactMapper::toContactedHouseCardDto)
                .toList());
        return contactedHousePageDto;
    }
}

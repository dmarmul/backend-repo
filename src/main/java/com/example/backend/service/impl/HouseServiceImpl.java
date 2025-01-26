package com.example.backend.service.impl;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.house.HouseDto;
import com.example.backend.dto.house.HouseFilterDto;
import com.example.backend.dto.house.HousePageDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.mapper.RealtorMapper;
import com.example.backend.model.House;
import com.example.backend.model.User;
import com.example.backend.model.UserContact;
import com.example.backend.repository.HouseRepository;
import com.example.backend.repository.UserContactRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.HouseService;
import com.example.backend.specification.HouseSpecification;
import jakarta.transaction.Transactional;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@RequiredArgsConstructor
@Service
public class HouseServiceImpl implements HouseService {
    private static final int DEFAULT_PAGE_SIZE = 15;
    private static final int DEFAULT_SEARCH_PAGE = 0;
    private static final int HOUSE_CARTS_PAGE_SIZE = 15;
    private static final int HOUSE_CARTS_LIMIT = 3;
    private static final String BUCKET_NAME = "smart-estate-backend";
    private static final String KEY_NAME = "bye/";
    private static final String REGION = "us-east-1";
    private final UserContactRepository contactRepository;
    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final UserRepository userRepository;
    private final RealtorMapper realtorMapper;

    @Override
    public HouseDto findBuyHouseById(User user, Long id) {
        House house = checkHouse(id);
        if (house.isRentHouse()) {
            throw new EntityNotFoundException("Can't find house to buy by id: " + id);
        }

        return findHouseById(user, id, house);
    }

    @Override
    public HouseDto findRentHouseById(User user, Long id) {
        House house = checkHouse(id);
        if (!house.isRentHouse()) {
            throw new EntityNotFoundException("Can't find house for rent by id: " + id);
        }
        return findHouseById(user, id, house);
    }

    @Override
    public HousePageDto findAllBuyHouses(User user, HouseFilterDto filterDto,
                                         Sort sort, Pageable pageable) {
        if (pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageable.getPageNumber() - 1, DEFAULT_PAGE_SIZE);
        }
        Specification<House> spec = Specification.where(null);
        spec = spec.and(HouseSpecification.isNotRentHouse());

        return findAllHouses(user, filterDto, sort, pageable, spec);
    }

    @Override
    public HousePageDto findAllRentHouses(User user, HouseFilterDto filterDto,
                                          Sort sort, Pageable pageable) {
        if (pageable.getPageNumber() > 0) {
            pageable = PageRequest.of(pageable.getPageNumber() - 1, DEFAULT_PAGE_SIZE);
        }
        Specification<House> spec = Specification.where(null);
        spec = spec.and(HouseSpecification.isRentHouse());

        return findAllHouses(user, filterDto, sort, pageable, spec);
    }

    @Override
    @Transactional
    public void likeBuyHouseById(User user, Long houseId) {
        User savedUser = checkUser(user);
        House house = checkHouse(houseId);
        if (house.isRentHouse()) {
            throw new EntityNotFoundException("Can't find house to buy by id: " + houseId);
        }

        likeHouseById(savedUser, house);
    }

    @Override
    public void likeRentHouseById(User user, Long houseId) {
        User savedUser = checkUser(user);
        House house = checkHouse(houseId);
        if (!house.isRentHouse()) {
            throw new EntityNotFoundException("Can't find house for rent by id: " + houseId);
        }

        likeHouseById(savedUser, house);
    }

    public void addContact(User user, Long houseId) {
        checkUser(user);
        House house = checkHouse(houseId);
        if (contactRepository.existsByUserAndHouse(user, house)) {
            return;
        }

        UserContact contact = new UserContact();
        contact.setUser(user);
        contact.setHouse(house);
        contact.setContactDate(LocalDateTime.now());

        contactRepository.save(contact);
    }

    String setUrl(String photoName) {
        S3Client s3 = S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(REGION))
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        URL url = s3.utilities().getUrl(GetUrlRequest.builder()
                .bucket(BUCKET_NAME)
                .key(KEY_NAME + photoName)
                .build());

        return url.toString();
    }

    private HouseDto findHouseById(User user, Long id, House house) {
        house.getPhotoLinks()
                .forEach(photoLink ->
                        photoLink.setPhotoLink(setUrl(photoLink.getPhotoLink())));
        HouseDto houseDto = houseMapper.toDto(house);
        if (user != null) {
            Optional<Long> optionalId = checkUser(user).getLikedHouses().stream()
                    .map(House::getId)
                    .filter(houseId -> Objects.equals(houseId, id))
                    .findFirst();
            houseDto.setIsLiked(optionalId.isPresent());
        }
        houseDto.setRealtorDto(realtorMapper.toDto(house.getRealtor()));
        houseDto.setHouseCarts(houseRepository
                .findAll(PageRequest.of(DEFAULT_SEARCH_PAGE, HOUSE_CARTS_LIMIT))
                .stream()
                .peek(findedHouse ->
                        findedHouse.getPhotoLinks()
                                .forEach(photoLink ->
                                        photoLink.setPhotoLink(setUrl(photoLink.getPhotoLink()))
                                )
                ).map(houseMapper::toHouseCartDto)
                .toList());

        return houseDto;
    }

    private HousePageDto findAllHouses(User user, HouseFilterDto filterDto,
                                       Sort sort, Pageable pageable, Specification<House> spec) {
        if (filterDto.getHouseType() != null) {
            spec = spec.and(HouseSpecification.houseTypeEquals(filterDto.getHouseType()));
        }
        if (filterDto.getMaxPrice() != null) {
            spec = spec.and(HouseSpecification.priceLessThan(filterDto.getMaxPrice()));
        }
        if (filterDto.getMinPrice() != null) {
            spec = spec.and(HouseSpecification.priceGreaterThan(filterDto.getMinPrice()));
        }
        if (filterDto.getRooms() != null) {
            spec = spec.and(HouseSpecification.roomsEquals(filterDto.getRooms()));
        }
        if (filterDto.getBedrooms() != null) {
            spec = spec.and(HouseSpecification.bedroomsEquals(filterDto.getBedrooms()));
        }
        if (filterDto.getFeatures() != null && !filterDto.getFeatures().isEmpty()) {
            spec = spec.and(HouseSpecification.featuresIn(filterDto.getFeatures()));
        }
        if (filterDto.getNeighborhoods() != null && !filterDto.getNeighborhoods().isEmpty()) {
            spec = spec.and(HouseSpecification.neighborhoodsIn(filterDto.getNeighborhoods()));
        }

        List<HouseCartDto> houseCarts = houseRepository.findAll(spec, pageable).stream()
                .peek(house ->
                        house.getPhotoLinks()
                                .forEach(photoLink ->
                                        photoLink.setPhotoLink(setUrl(photoLink.getPhotoLink()))
                                )
                ).map(houseMapper::toHouseCartDto)
                .toList();
        if (user != null) {
            List<Long> longList = checkUser(user).getLikedHouses().stream()
                    .map(House::getId)
                    .toList();
            houseCarts.forEach(dto -> dto.setIsLiked(longList.contains(dto.getId())));
        }

        HousePageDto housePageDto = new HousePageDto();
        housePageDto.setHouseCartDtoList(houseCarts);
        int maxHouseNumber = (int) Math.ceil((double) houseRepository.count(spec));
        housePageDto.setMaxHouseNumber(maxHouseNumber);
        housePageDto.setMaxPageNumber(maxHouseNumber > 0 && maxHouseNumber < HOUSE_CARTS_PAGE_SIZE
                ? 1 : maxHouseNumber / HOUSE_CARTS_PAGE_SIZE);
        return housePageDto;
    }

    private void likeHouseById(User savedUser, House house) {
        Set<House> likedHouses = savedUser.getLikedHouses();
        if (!likedHouses.contains(house)) {
            likedHouses.add(house);
        } else {
            likedHouses.remove(house);
        }
    }

    private House checkHouse(Long id) {
        return houseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find house by id: " + id));
    }

    private User checkUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + user.getEmail()));
    }
}

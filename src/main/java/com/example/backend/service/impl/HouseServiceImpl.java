package com.example.backend.service.impl;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.house.HouseDto;
import com.example.backend.dto.house.HouseFilterDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.model.House;
import com.example.backend.model.User;
import com.example.backend.repository.HouseRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.HouseService;
import com.example.backend.specification.HouseSpecification;
import jakarta.transaction.Transactional;
import java.net.URL;
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
    private static final int DEFAULT_SEARCH_PAGE = 0;
    private static final int HOUSE_CARTS_LIMIT = 3;
    private static final String BUCKET_NAME = "smart-estate-backend";
    private static final String KEY_NAME = "bye/";
    private static final String REGION = "us-east-1";
    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;
    private final UserRepository userRepository;

    @Override
    public HouseDto findById(User user, Long id) {
        House house = checkHouse(id);
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

    @Override
    public List<HouseCartDto> findAll(User user, HouseFilterDto filterDto,
                                      Sort sort, Pageable pageable) {
        Specification<House> spec = Specification.where(null);

        if (filterDto.getHouseType() != null) {
            spec = spec.and(HouseSpecification.houseTypeEquals(filterDto.getHouseType()));
        }
        if (filterDto.getMinPrice() != null) {
            spec = spec.and(HouseSpecification.priceLessThan(filterDto.getMinPrice()));
        }
        if (filterDto.getMaxPrice() != null) {
            spec = spec.and(HouseSpecification.priceLessThan(filterDto.getMaxPrice()));
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
        if (filterDto.getNeighborhood() != null) {
            spec = spec.and(HouseSpecification.neighborhoodEquals(filterDto.getNeighborhood()));
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
        return houseCarts;
    }

    @Override
    @Transactional
    public void likeHouseById(User user, Long id) {
        User savedUser = checkUser(user);
        House house = checkHouse(id);

        Set<House> likedHouses = savedUser.getLikedHouses();
        if (!likedHouses.contains(house)) {
            likedHouses.add(house);
        } else {
            likedHouses.remove(house);
        }
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

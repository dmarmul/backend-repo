package com.example.backend.service.impl;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.model.House;
import com.example.backend.repository.HouseRepository;
import com.example.backend.service.HouseService;
import java.net.URL;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@RequiredArgsConstructor
@Service
public class HouseServiceImpl implements HouseService {
    private static final String BUCKET_NAME = "smart-estate-backend";
    private static final String KEY_NAME = "bye/";
    private static final String REGION = "us-east-1";
    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    @Override
    public HouseDto findById(Long id) {
        House house = houseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find house by id: " + id));
        house.getPhotoLinks()
                .forEach(photoLink ->
                        photoLink.setPhotoLink(setUrl(photoLink.getPhotoLink())));
        return houseMapper.toDto(house);
    }

    @Override
    public List<HouseCartDto> findAll(Sort sort, Pageable pageable) {
        return houseRepository.findAll(pageable).stream()
                .peek(house ->
                        house.getPhotoLinks()
                                .forEach(photoLink ->
                                        photoLink.setPhotoLink(setUrl(photoLink.getPhotoLink()))
                                )
                ).map(houseMapper::toHouseCartDto)
                .toList();
    }

    private String setUrl(String photoName) {
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
}
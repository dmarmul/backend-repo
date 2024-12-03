package com.example.backend.service.impl;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.mapper.HouseMapper;
import com.example.backend.model.House;
import com.example.backend.repository.HouseRepository;
import com.example.backend.service.HouseService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.backend.exception.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HouseServiceImpl implements HouseService {
    private final HouseRepository houseRepository;
    private final HouseMapper houseMapper;

    @Override
    public HouseDto findById(Long id) {
        House house = houseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find house by id: " + id));
        return houseMapper.toDto(house);
    }

    @Override
    @Transactional//заменить
    public List<HouseCartDto> findAll(Sort sort, Pageable pageable) {
        return houseRepository.findAll(pageable).stream()
                .map(houseMapper::toHouseCartDto)
                .toList();
    }
}

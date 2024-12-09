package com.example.backend.service;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.dto.HouseFilterDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface HouseService {
    HouseDto findById(Long id);

    List<HouseCartDto> findAll(HouseFilterDto filterDto, Sort sort, Pageable pageable);
}

package com.example.backend.service;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface HouseService {
    HouseDto findById(Long id);

    List<HouseCartDto> findAll(Sort sort, Pageable pageable);
}

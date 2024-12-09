package com.example.backend.service;

import com.example.backend.dto.NeighborhoodCartDto;
import com.example.backend.dto.NeighborhoodDto;
import java.util.List;

public interface NeighborhoodService {
    List<NeighborhoodCartDto> findAll();

    NeighborhoodDto findById(Long id);
}

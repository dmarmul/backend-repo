package com.example.backend.service;

import com.example.backend.dto.neighborhood.NeighborhoodCartDto;
import com.example.backend.dto.neighborhood.NeighborhoodDto;
import java.util.List;

public interface NeighborhoodService {
    List<NeighborhoodCartDto> findAll();

    NeighborhoodDto findById(Long id);
}

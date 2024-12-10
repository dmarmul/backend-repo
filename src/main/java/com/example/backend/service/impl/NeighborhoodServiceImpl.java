package com.example.backend.service.impl;

import com.example.backend.dto.neighborhood.NeighborhoodCartDto;
import com.example.backend.dto.neighborhood.NeighborhoodDto;
import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.mapper.NeighborhoodMapper;
import com.example.backend.repository.NeighborhoodRepository;
import com.example.backend.service.NeighborhoodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NeighborhoodServiceImpl implements NeighborhoodService {
    private final NeighborhoodRepository neighborhoodRepository;
    private final NeighborhoodMapper neighborhoodMapper;

    @Override
    public List<NeighborhoodCartDto> findAll() {
        return neighborhoodRepository.findAll().stream()
                .map(neighborhoodMapper::toCartDto)
                .toList();
    }

    @Override
    public NeighborhoodDto findById(Long id) {
        return neighborhoodMapper.toDto(
                neighborhoodRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't find neighborhood by id: " + id)));
    }
}

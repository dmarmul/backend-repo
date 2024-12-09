package com.example.backend.controller;

import com.example.backend.dto.NeighborhoodCartDto;
import com.example.backend.dto.NeighborhoodDto;
import com.example.backend.service.NeighborhoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Neighborhoods management", description = "Endpoints for managing neighborhoods")
@RequiredArgsConstructor
@RestController
@RequestMapping("residential/neighborhoods")
public class NeighborhoodController {
    private final NeighborhoodService neighborhoodService;

    @GetMapping
    @Operation(summary = "Get all neighborhoods",
            description = "Get a list of all neighborhoods")
    public List<NeighborhoodCartDto> getAll() {
        return neighborhoodService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get neighborhoods", description = "Get one neighborhood by id")
    public NeighborhoodDto getNeighborhoodById(@PathVariable Long id) {
        return neighborhoodService.findById(id);
    }
}

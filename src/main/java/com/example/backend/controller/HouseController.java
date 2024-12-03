package com.example.backend.controller;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.service.HouseService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "House management", description = "Endpoints for managing houses")
@RequiredArgsConstructor
@RestController
@RequestMapping("/houses")
public class HouseController {
    private final HouseService houseService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all houses",
            description = "Get a list of all available houses")
    public List<HouseCartDto> getAll(Sort sort, Pageable pageable) {
        return houseService.findAll(sort, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get house", description = "Get one house by id")
    public HouseDto getBookById(@PathVariable Long id) {
        return houseService.findById(id);
    }
}

package com.example.backend.controller;

import com.example.backend.dto.HouseCartDto;
import com.example.backend.dto.HouseDto;
import com.example.backend.dto.HouseFilterDto;
import com.example.backend.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "House management", description = "Endpoints for managing houses")
@RequiredArgsConstructor
@RestController
@RequestMapping("residential/buy")
public class HouseController {
    private final HouseService houseService;

    @GetMapping
    @Operation(summary = "Get all houses",
            description = "Get a list of all available houses with filter params if specified")
    public List<HouseCartDto> getAll(@ModelAttribute HouseFilterDto filterDto,
                                     Sort sort, Pageable pageable) {
        return houseService.findAll(filterDto, sort, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get house", description = "Get one house by id")
    public HouseDto getBookById(@PathVariable Long id) {
        return houseService.findById(id);
    }
}

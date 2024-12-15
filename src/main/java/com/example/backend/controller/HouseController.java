package com.example.backend.controller;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.house.HouseDto;
import com.example.backend.dto.house.HouseFilterDto;
import com.example.backend.model.User;
import com.example.backend.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "House management", description = "Endpoints for managing houses")
@RequiredArgsConstructor
@RestController
@RequestMapping("residential/buy")
public class HouseController {
    private static final int DEFAULT_PAGE_SIZE = 15;

    private final HouseService houseService;

    @GetMapping
    @Operation(summary = "Get all houses",
            description = "Get a list of all available houses with filter params if specified")
    public List<HouseCartDto> getAll(@AuthenticationPrincipal User user,
                                     @ModelAttribute HouseFilterDto filterDto,
                                     Sort sort, Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE);

        return houseService.findAll(user, filterDto, sort, fixedPageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get house", description = "Get one house by id")
    public HouseDto getHouseById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return houseService.findById(user, id);
    }

    @PostMapping("/saved/{id}")
    @Operation(summary = "Add liked house", description = "Add liked house by id")
    public void likedHouseById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        houseService.likeHouseById(user, id);
    }
}

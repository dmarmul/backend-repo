package com.example.backend.controller;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("profile")
public class UserController {
    private static final int DEFAULT_PAGE_SIZE = 4;

    private final UserService userService;

    @GetMapping("/saved-props")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all liked houses",
            description = "Get a list of all available houses with filter params if specified")
    public List<HouseCartDto> getAllLikedHouses(@AuthenticationPrincipal User user,
                                                Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), DEFAULT_PAGE_SIZE);

        return userService.findAllLikedHouses(user, fixedPageable);
    }
}

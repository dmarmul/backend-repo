package com.example.backend.controller;

import com.example.backend.model.User;
import com.example.backend.service.RealtorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Realtor management", description = "Endpoints for managing realtors")
@RequiredArgsConstructor
@RestController
@RequestMapping("realtors")
public class RealtorController {
    private final RealtorService realtorService;

    @PostMapping("/{id}")
    @Operation(summary = "Add contacted realtor", description = "Add contacted realtor by id")
    public void addContactedRealtorById(@AuthenticationPrincipal User user, @PathVariable Long id) {
        realtorService.addContactedRealtorById(user, id);
    }
}

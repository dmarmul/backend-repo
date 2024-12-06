package com.example.backend.dto;

import com.example.backend.model.Feature;
import com.example.backend.model.House;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateHouseRequestDto {
    @NotNull
    private House.HouseType houseType;
    @NotNull
    @Min(1)
    @Max(10)
    private int rooms;
    @NotNull
    @Min(0)
    @Max(10)
    private int bedrooms;
    @NotNull
    @Min(10)
    @Max(1000)
    private int houseSquare;
    @NotNull
    @Min(0)
    @Max(6000)
    private BigDecimal price;
    @NotBlank
    @Size(max = 1000, message = "Max description size is 1000 symbols")
    private String description;
    @Size(max = 15, message = "Max number of photos is 15")
    @NotEmpty(message = "There must to be at least 1 photo")
    private Set<String> photoLinks;
    private Set<Feature> features;
}

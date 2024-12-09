package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseFilterDto {
    private String houseType;
    private Integer rooms;
    private Integer bedrooms;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Set<String> features;
}

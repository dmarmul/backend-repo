package com.example.backend.dto.house;

import com.example.backend.model.House;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HouseDto {
    private Long id;
    private String street;
    private House.HouseType houseType;
    private int rooms;
    private int bedrooms;
    private int houseSquare;
    private BigDecimal price;
    private String description;
    private Set<String> photoLinks;
    private Set<String> features;
}

package com.example.backend.dto;

import com.example.backend.model.Feature;
import com.example.backend.model.HouseType;
import com.example.backend.model.PhotoLink;
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
    private HouseType houseType;
    private int rooms;
    private int bedrooms;
    private int houseSquare;
    private BigDecimal price;
    private String description;
    private Set<PhotoLink> photoLinks;
    private Set<Feature> features;
}

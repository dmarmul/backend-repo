package com.example.backend.dto;

import com.example.backend.model.PhotoLink;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HouseCartDto {
    private Long id;
    private String houseType;
    private int rooms;
    private int bedrooms;
    private int houseSquare;
    private BigDecimal price;
    private Set<PhotoLink> photoLinks;
}

package com.example.backend.dto.house;

import com.example.backend.model.House;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HouseCartDto {
    private Long id;
    private String street;
    private House.HouseType houseType;
    private int rooms;
    private int bedrooms;
    private int houseSquare;
    private BigDecimal price;
    private String photoUrl;
    private Boolean isLiked = false;
}

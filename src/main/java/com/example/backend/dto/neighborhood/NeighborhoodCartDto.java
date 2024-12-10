package com.example.backend.dto.neighborhood;

import com.example.backend.model.Neighborhood;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NeighborhoodCartDto {
    private Long id;
    private Neighborhood.NeighborhoodName neighborhoodName;
    private String photoLink;
}

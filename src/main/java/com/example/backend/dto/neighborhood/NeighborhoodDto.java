package com.example.backend.dto.neighborhood;

import com.example.backend.model.Neighborhood;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NeighborhoodDto {
    private Long id;
    private Neighborhood.NeighborhoodName neighborhoodName;
    private String description;
    private String topAttractions;
    private String photoLink;
    private List<TransportDetailsDto> transportDetails;
    private List<ParkingPlaceDto> parkingPlace;
    private List<KeyDetailsDto> keyDetails;
}

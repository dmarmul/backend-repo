package com.example.backend.dto.house;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LickedHousePageDto {
    private List<HouseCartDto> houseCartDtoList;
    private int maxPageNumber;
}

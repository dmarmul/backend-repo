package com.example.backend.dto.house;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HousePageDto {
    private List<HouseCartDto> houseCartDtoList;
    private int maxPageNumber;
    private int maxHouseNumber;
}

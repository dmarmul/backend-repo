package com.example.backend.dto.house;

import com.example.backend.dto.realtor.RealtorCartDto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContactedHouseCartDto {
    private HouseCartDto houseCartDto;
    private LocalDateTime contactDate;
    private RealtorCartDto realtorCartDto;
}

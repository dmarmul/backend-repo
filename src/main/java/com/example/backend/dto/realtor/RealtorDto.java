package com.example.backend.dto.realtor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RealtorDto {
    private Long id;
    private String email;
    private String name;
    private String agencyName;
    private String phoneNumber;
    private String photoUrl;
}

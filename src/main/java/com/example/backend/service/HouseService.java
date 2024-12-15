package com.example.backend.service;

import com.example.backend.dto.house.HouseCartDto;
import com.example.backend.dto.house.HouseDto;
import com.example.backend.dto.house.HouseFilterDto;
import com.example.backend.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface HouseService {
    HouseDto findById(User user, Long id);

    List<HouseCartDto> findAll(User user, HouseFilterDto filterDto, Sort sort, Pageable pageable);

    void likeHouseById(User user, Long id);
}

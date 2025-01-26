package com.example.backend.service;

import com.example.backend.dto.house.HouseDto;
import com.example.backend.dto.house.HouseFilterDto;
import com.example.backend.dto.house.HousePageDto;
import com.example.backend.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface HouseService {
    HouseDto findBuyHouseById(User user, Long id);

    HousePageDto findAllBuyHouses(User user, HouseFilterDto filterDto,
                                  Sort sort, Pageable pageable);

    void likeBuyHouseById(User user, Long id);

    void addContact(User user, Long id);

    HousePageDto findAllRentHouses(User user, HouseFilterDto filterDto,
                                   Sort sort, Pageable pageable);

    HouseDto findRentHouseById(User user, Long id);

    void likeRentHouseById(User user, Long id);
}

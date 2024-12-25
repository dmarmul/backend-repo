package com.example.backend.repository;

import com.example.backend.model.House;
import com.example.backend.model.User;
import com.example.backend.model.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContactRepository extends JpaRepository<UserContact, Long> {
    boolean existsByUserAndHouse(User user, House house);
}

package com.example.backend.service;

import com.example.backend.model.User;

public interface RealtorService {
    void addContactedRealtorById(User user, Long id);
}

package com.example.backend.service.impl;

import com.example.backend.exception.EntityNotFoundException;
import com.example.backend.model.Realtor;
import com.example.backend.model.User;
import com.example.backend.repository.RealtorRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.RealtorService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RealtorServiceImpl implements RealtorService {
    private final UserRepository userRepository;
    private final RealtorRepository realtorRepository;

    @Override
    @Transactional
    public void addContactedRealtorById(User user, Long id) {
        Set<Realtor> realtors = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find user by email: " + user.getEmail()))
                .getRealtors();
        Realtor realtor = realtorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find realtor by id: " + id));

        realtors.add(realtor);
    }
}

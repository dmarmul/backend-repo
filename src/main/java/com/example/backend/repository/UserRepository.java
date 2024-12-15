package com.example.backend.repository;

import com.example.backend.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"roles", "likedHouses"})
    Optional<User> findByEmail(String email);
}

package com.example.backend.repository;

import com.example.backend.model.Neighborhood;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
    @EntityGraph(attributePaths = "keyDetails")
    @NonNull
    @Override
    Optional<Neighborhood> findById(@NonNull Long id);
}

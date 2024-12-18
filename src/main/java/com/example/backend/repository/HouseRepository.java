package com.example.backend.repository;

import com.example.backend.model.House;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface HouseRepository extends JpaRepository<House, Long> {
    @EntityGraph(attributePaths = {"photoLinks", "features"})
    @NonNull
    @Override
    Optional<House> findById(@NonNull Long id);

    @EntityGraph(attributePaths = "photoLinks")
    @NonNull
    Page<House> findAll(Specification<House> spec, @NonNull Pageable pageable);
}

package com.example.backend.repository;

import com.example.backend.model.House;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface HouseRepository extends JpaRepository<House, Long> {
    @NonNull
    @Override
    Optional<House> findById(@NonNull Long id);

    @NonNull
    @Override
    Page<House> findAll(@NonNull Pageable pageable);
}

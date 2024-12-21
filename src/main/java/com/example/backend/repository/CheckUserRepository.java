package com.example.backend.repository;

import com.example.backend.model.CheckMailUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckUserRepository extends JpaRepository<CheckMailUser, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndCheckedNumber(String email, int checkedNumber);
}

package com.candlekart.auth_service.repository;

import com.candlekart.auth_service.model.AuthDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<AuthDetails,Long> {
    AuthDetails findByUsername(String username);
    boolean existsByUsername(String username);
}

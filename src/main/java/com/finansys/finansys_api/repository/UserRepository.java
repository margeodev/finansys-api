package com.finansys.finansys_api.repository;

import com.finansys.finansys_api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumberAndIsActiveTrue(String phoneNumber);
    Optional<User> findByEmailAndIsActiveTrue(String phoneNumber);
}

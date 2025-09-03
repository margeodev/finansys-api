package com.finansys.finansysapi.domain.repository;

import com.finansys.finansysapi.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    Optional<User> findByPhoneNumberAndIsActiveTrue(String phoneNumber);
    Optional<User> findByEmailAndIsActiveTrue(String phoneNumber);
}

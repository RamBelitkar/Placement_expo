package com.placement.expo.repository;

import com.placement.expo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAppwriteId(String appwriteId);
    boolean existsByEmail(String email);
}

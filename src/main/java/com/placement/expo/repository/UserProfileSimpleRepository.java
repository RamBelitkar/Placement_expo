package com.placement.expo.repository;

import com.placement.expo.entity.UserProfileSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileSimpleRepository extends JpaRepository<UserProfileSimple, Long> {
    
    Optional<UserProfileSimple> findByAppwriteUserId(String appwriteUserId);
    
    boolean existsByAppwriteUserId(String appwriteUserId);
    
    Optional<UserProfileSimple> findByEmail(String email);
}

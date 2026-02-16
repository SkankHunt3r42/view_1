package com.example.test_tasks.repo;

import com.example.test_tasks.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("UPDATE UserEntity SET is_verified = true WHERE email = :email")
    @Modifying
    void updateUserVerifiedStatus(String email);


}

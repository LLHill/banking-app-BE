package com.bankin.app.repository;

import com.bankin.app.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByPhoneNumber(String phoneNumber);

    Optional<AppUser> findByEmailOrPhoneNumber (String email, String phoneNumber);
}

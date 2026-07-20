package com.freefire.curtidas.repository;

import com.freefire.curtidas.entity.User;
import com.freefire.curtidas.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByApiKey(String apiKey);
    List<User> findByCountry(CountryCode country);
    List<User> findByIsActive(Boolean isActive);
}

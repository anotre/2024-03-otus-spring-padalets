package ru.otus.hw.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.otus.hw.models.security.User;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
}

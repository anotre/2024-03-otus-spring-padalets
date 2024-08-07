package ru.otus.hw.repositories.security;//package ru.otus.hw.repositories.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.security.SimpleUser;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<SimpleUser, String> {
    Optional<SimpleUser> findByUsername(String username);
}

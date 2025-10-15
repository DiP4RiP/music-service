package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}

package ru.sbrf.mininotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.mininotes.domain.User;

import java.util.Optional;

/**
 * Интрефейс репозитория для класса User.
 */
public interface UserRepository extends JpaRepository<ru.sbrf.mininotes.domain.User, Integer> {
    Optional<User> getUserByUsername(String username);

}

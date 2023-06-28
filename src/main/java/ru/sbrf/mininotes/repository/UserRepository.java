package ru.sbrf.mininotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.mininotes.domain.User;
/**
 * Интрефейс репозитория для класса User.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}

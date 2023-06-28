package ru.sbrf.mininotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.mininotes.domain.Project;
/**
 * Интрефейс репозитория для класса Project.
 */
public interface ProjectRepository extends JpaRepository<Project, Integer> {
}

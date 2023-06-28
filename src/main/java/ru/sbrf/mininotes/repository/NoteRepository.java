package ru.sbrf.mininotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sbrf.mininotes.domain.Note;

/**
 * Интрефейс репозитория для класса Note.
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {
}

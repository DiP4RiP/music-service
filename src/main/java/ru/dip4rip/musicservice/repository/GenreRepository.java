package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}

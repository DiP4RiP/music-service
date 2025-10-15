package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.Composer;

public interface ComposerRepository extends JpaRepository<Composer, Integer> {
}

package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.Music;

public interface MusicRepository extends JpaRepository<Music, Long> {
}

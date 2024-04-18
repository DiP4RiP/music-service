package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}

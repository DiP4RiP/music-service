package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.PlaylistMusic;
import ru.dip4rip.musicservice.models.PlaylistMusicId;

public interface PlaylistMusicRepository extends JpaRepository<PlaylistMusic, PlaylistMusicId> {
}

package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.MediaType;

public interface MediaTypeRepository extends JpaRepository<MediaType, Integer> {
}

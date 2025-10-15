package ru.dip4rip.musicservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dip4rip.musicservice.models.RecordLabel;

public interface RecordLabelRepository extends JpaRepository<RecordLabel, Integer> {
}

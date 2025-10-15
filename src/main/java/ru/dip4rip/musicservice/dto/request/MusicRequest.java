package ru.dip4rip.musicservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicRequest {
    private Integer genreId;
    private String title;
    private Integer performerId;
    private Integer composerId;
    private Integer mediaTypeId;
    private Integer recordLabelId;
    private LocalDate recordingDate;
}

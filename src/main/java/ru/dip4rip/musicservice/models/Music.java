package ru.dip4rip.musicservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "music")
public class Music {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "inventory_number", nullable = false)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "genre_id")
  private Genre genre;

  @Column(name = "title", length = 100)
  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "performer_id")
  private Performer performer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "composer_id")
  private Composer composer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "media_type_id")
  private MediaType mediaType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "record_label_id")
  private RecordLabel recordLabel;

  @Column(name = "recording_date")
  private LocalDate recordingDate;

  @OneToMany(mappedBy = "music", fetch = FetchType.LAZY)
  private Set<PlaylistMusic> musics;
}
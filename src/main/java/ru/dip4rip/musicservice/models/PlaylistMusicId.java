package ru.dip4rip.musicservice.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class PlaylistMusicId implements Serializable {
  private static final long serialVersionUID = 5845435210692663945L;
  @Column(name = "playlist_id", nullable = false)
  private Integer playlistId;

  @Column(name = "inventory_number", nullable = false)
  private Integer inventoryNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PlaylistMusicId entity = (PlaylistMusicId) o;
    return Objects.equals(this.playlistId, entity.playlistId) &&
        Objects.equals(this.inventoryNumber, entity.inventoryNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playlistId, inventoryNumber);
  }

}
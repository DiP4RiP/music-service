package ru.dip4rip.musicservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.dip4rip.musicservice.converter.MusicConverter;
import ru.dip4rip.musicservice.dto.request.MusicRequest;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.models.Composer;
import ru.dip4rip.musicservice.models.Genre;
import ru.dip4rip.musicservice.models.MediaType;
import ru.dip4rip.musicservice.models.Music;
import ru.dip4rip.musicservice.models.Performer;
import ru.dip4rip.musicservice.models.RecordLabel;
import ru.dip4rip.musicservice.repository.ComposerRepository;
import ru.dip4rip.musicservice.repository.GenreRepository;
import ru.dip4rip.musicservice.repository.MediaTypeRepository;
import ru.dip4rip.musicservice.repository.MusicRepository;
import ru.dip4rip.musicservice.repository.PerformerRepository;
import ru.dip4rip.musicservice.repository.RecordLabelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicService {

  MusicRepository musicRepository;
  MusicConverter musicConverter;
  GenreRepository genreRepository;
  PerformerRepository performerRepository;
  ComposerRepository composerRepository;
  MediaTypeRepository mediaTypeRepository;
  RecordLabelRepository recordLabelRepository;
  EntityManager entityManager;

  public MusicResponse findById(long id) {
    return musicRepository.findById(id)
        .map(musicConverter::toDto)
        .orElseThrow(() -> new RuntimeException(String.format("Музыка с номером: %s не найдена", id)));
  }

  public List<MusicResponse> findAll() {
    return musicRepository.findAll().stream().map(musicConverter::toDto).toList();
  }

  public MusicResponse create(MusicRequest musicRequest) {
    Music music = new Music();
    music.setTitle(musicRequest.getTitle());
    music.setRecordingDate(musicRequest.getRecordingDate());

    // Устанавливаем связанные сущности
    if (musicRequest.getGenreId() != null) {
      Genre genre = genreRepository.findById(musicRequest.getGenreId())
          .orElseThrow(() -> new RuntimeException("Жанр не найден"));
      music.setGenre(genre);
    }

    if (musicRequest.getPerformerId() != null) {
      Performer performer = performerRepository.findById(musicRequest.getPerformerId())
          .orElseThrow(() -> new RuntimeException("Исполнитель не найден"));
      music.setPerformer(performer);
    }

    if (musicRequest.getComposerId() != null) {
      Composer composer = composerRepository.findById(musicRequest.getComposerId())
          .orElseThrow(() -> new RuntimeException("Композитор не найден"));
      music.setComposer(composer);
    }

    if (musicRequest.getMediaTypeId() != null) {
      MediaType mediaType = mediaTypeRepository.findById(musicRequest.getMediaTypeId())
          .orElseThrow(() -> new RuntimeException("Тип носителя не найден"));
      music.setMediaType(mediaType);
    }

    if (musicRequest.getRecordLabelId() != null) {
      RecordLabel recordLabel = recordLabelRepository.findById(musicRequest.getRecordLabelId())
          .orElseThrow(() -> new RuntimeException("Лейбл не найден"));
      music.setRecordLabel(recordLabel);
    }

    Music savedMusic = musicRepository.save(music);
    return musicConverter.toDto(savedMusic);
  }

  public void deleteById(long id) {
    if (!musicRepository.existsById(id)) {
      throw new RuntimeException(String.format("Музыка с номером: %s не найдена", id));
    }
    musicRepository.deleteById(id);
  }

  public List<MusicResponse> searchMusic(String query) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Music> cq = cb.createQuery(Music.class);
    Root<Music> music = cq.from(Music.class);
    
    // Создаем предикаты для поиска по разным полям
    String searchPattern = "%" + query.toLowerCase() + "%";
    
    Predicate titlePredicate = cb.like(cb.lower(music.get("title")), searchPattern);
    Predicate performerPredicate = cb.like(cb.lower(music.get("performer").get("name")), searchPattern);
    Predicate composerPredicate = cb.like(cb.lower(music.get("composer").get("name")), searchPattern);
    Predicate recordLabelPredicate = cb.like(cb.lower(music.get("recordLabel").get("name")), searchPattern);
    
    // Объединяем предикаты с помощью OR
    Predicate searchPredicate = cb.or(
        titlePredicate,
        performerPredicate,
        composerPredicate,
        recordLabelPredicate
    );
    
    cq.where(searchPredicate);
    
    List<Music> results = entityManager.createQuery(cq).getResultList();
    return results.stream().map(musicConverter::toDto).toList();
  }
}

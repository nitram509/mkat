package de.nitram509.mkat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.Movie;
import de.nitram509.mkat.repository.Migrated;
import de.nitram509.mkat.repository.MigrationService;
import de.nitram509.mkat.repository.mkat.FilesRepository;
import de.nitram509.mkat.repository.mkat.MediasRepository;
import de.nitram509.mkat.repository.mkat.MoviesRepository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class MigrationLearningTest {

  private MediasRepository mediasRepository;
  private MoviesRepository moviesRepository;
  private FilesRepository filesRepository;
  private MigrationService migrationService;

  List<Integer> blacklist = new ArrayList<>();
  List<Integer> mergelist = new ArrayList<>();

  @BeforeMethod
  public void setup() {
    Injector injector = Guice.createInjector(
        new MkatAppModule()
    );
    mediasRepository = injector.getInstance(MediasRepository.class);
    moviesRepository = injector.getInstance(MoviesRepository.class);
    filesRepository = injector.getInstance(FilesRepository.class);
    migrationService = injector.getInstance(MigrationService.class);
  }

  @Test(enabled = false)
  public void assume_we_have_2600_entries_in_MEDIAS() {
    List<Media> medias = mediasRepository.findAll();
    assertThat(medias.size()).isEqualTo(2605);
  }

  @Test(enabled = false)
  public void migrate() {
    List<Media> medias = mediasRepository.findAll();
    List<Migrated> migrateds = migrationService.migrateAll(medias);

    migrateSimpleMedias(migrateds);
    migrateCd1Cd2Medias(migrateds);

  }

  private void migrateSimpleMedias(List<Migrated> migrateds) {
    Iterator<Migrated> it = migrateds.iterator();
    while (it.hasNext()) {
      Migrated migrated = it.next();
      List<Movie> movieList = migrated.getMovies();
      List<Media> mediaList = migrated.getMedias();
      Media media = mediaList.get(0);

      if (movieList.size() == 1) {
        Movie movie = movieList.get(0);
        boolean multiple = isMultiple(movie.name);
        if (!multiple) {
          if (1 == 1) throw new IllegalStateException("Sollte nicht mehr passieren! " + migrated.getMedias().get(0).media_id);
          moviesRepository.save(movie);
          media.movie_id = movie.movie_id;
          mediasRepository.save(media);
          it.remove();
          continue;
        }
      }

      if (movieList.size() > 1) {
        migrateManyOneMedia(migrated);
      }

    }
  }

  private boolean isMultiple(String name) {
    final String lowerName = name.toLowerCase();
    return lowerName.contains("cd1")
        || lowerName.contains("cd2")
        || lowerName.contains("cd3")
        || lowerName.contains("cd4")
        || lowerName.contains("cd5")
        || lowerName.contains("cd6")
        || lowerName.contains("cd7");
  }

  private void migrateManyOneMedia(Migrated migrated) {
//    List<Media> mediaList = migrated.getMedias();
//    Media oldMedia = mediaList.get(0);
//    List<Movie> movieList = migrated.getMovies();
//    for (Movie movie : movieList) {
//      moviesRepository.save(movie);
//
//      Media media = oldMedia.clone();
//      media.movie_id = movie.movie_id;
//      mediasRepository.save(media);
//    }
//    mediasRepository.delete(oldMedia);
  }


  private void migrateCd1Cd2Medias(List<Migrated> migrateds) {
//    Iterator<Migrated> it = migrateds.iterator();
//    while (it.hasNext()) {
//      Migrated baseMigrated = it.next();
//      Media baseMedia = baseMigrated.getMedias().get(0);
//
//      if (baseMedia.movie_id != 0) {
//        continue;
//      }
//
//      if (!isMultiple(baseMedia.name)) {
//        throw new IllegalStateException("Arg!" + baseMedia.media_id);
//      }
//
//      it.remove();
//
//      List<Media> mediasForOneMovie = new ArrayList<>();
//      mediasForOneMovie.add(baseMedia);
//
//      String baseName = baseMedia.name.substring(0, baseMedia.name.length() - 1);
//      for (Migrated otherMigrated : migrateds) {
//        Media otherMedia = otherMigrated.getMedias().get(0);
//        if (otherMedia.name.startsWith(baseName) && otherMedia.name.length() == (baseName.length() + 1)) {
//          mediasForOneMovie.add(otherMedia);
//        }
//      }
//
//      String aggregatedDescription = null;
//      Date aggregatedUpdated = baseMedia.updated;
//      for (Media media : mediasForOneMovie) {
//        if (media.updated.after(aggregatedUpdated)) {
//          aggregatedUpdated = media.updated;
//        }
//        if (aggregatedDescription == null || aggregatedDescription.trim().isEmpty()) {
//          aggregatedDescription = media.description;
//        } else {
//          if (media.description == null || media.description.trim().isEmpty()) {
//            // alles cool, nichts machen
//          } else {
//            if (!blacklist.contains(baseMedia.media_id) && !mergelist.contains(baseMedia.media_id)) {
//              assertThat(aggregatedDescription)
//                  .describedAs("different description!!! baseMedia=" + baseMedia.media_id + " -- media=" + media.media_id)
//                  .isEqualTo(media.description);
//            }
//            if (blacklist.contains(baseMedia.media_id)) {
//              // alles cool, nichts machen -- ignorieren
//              // wir behalten den Inhalt von baseDescription
//            }
//            if (mergelist.contains(baseMedia.media_id)) {
//              aggregatedDescription += "\r\n";
//              aggregatedDescription += media.description;
//            }
//          }
//        }
//      }
//
//      Movie movie = baseMigrated.getMovies().get(0);
//      movie.description = aggregatedDescription;
//      movie.name = baseName.substring(0, baseName.length() - 2).trim();
//
//      moviesRepository.save(movie);
//      for (Media media : mediasForOneMovie) {
//        media.movie_id = movie.movie_id;
//        mediasRepository.save(media);
//      }
//    }
  }

}

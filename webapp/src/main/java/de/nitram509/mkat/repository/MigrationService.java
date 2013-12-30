package de.nitram509.mkat.repository;

import de.nitram509.mkat.model.Media;
import de.nitram509.mkat.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MigrationService {

  List<String> blacklist = new ArrayList<>();

  public MigrationService() {

  }

  public List<Migrated> migrateAll(List<Media> medias) {
    List<Migrated> result = new ArrayList<>();
    for (Media media : medias) {
      if (media.movie_id == 0) {
        Migrated migrate = migrate(media);
        result.add(migrate);
      }
    }
    return result;
  }

  public Migrated migrate(Media media) {
    return migrateSingle(media);
  }

//  private Migrated migrateMultiple(Media media) {
//    Migrated migrated = new Migrated(media);
//    String[] names = media.name.split(",");
//    for (String name : names) {
//      if (name.trim().length() > 0) {
//        Migrated singleMigrated = migrateSingle(media);
//        Movie movie = singleMigrated.getMovies().get(0);
//        movie.name = name.trim();
//        migrated.getMovies().add(movie);
//      }
//    }
//    return migrated;
//  }

  private Migrated migrateSingle(Media media) {
    Movie movie = new Movie();
    movie.name = media.name;
    movie.languages = media.languages;
    movie.format = media.media_type;
    movie.description = media.description;
    movie.updated = media.updated;
    return new Migrated(media, movie);
  }

}
